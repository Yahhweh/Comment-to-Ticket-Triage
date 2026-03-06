package ibm.organisation.commenttotickettriage.service;

import ibm.organisation.commenttotickettriage.entity.Comment;
import ibm.organisation.commenttotickettriage.service.dto.TicketDto;
import ibm.organisation.commenttotickettriage.service.examples.ExamplesLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Component
public class AiService {

    private final ChatClient chatClient;
    private final TicketService ticketService;
    private final Resource promptResource;

    public AiService(ChatClient.Builder chatClientBuilder,
                     TicketService ticketService,
                     ExamplesLoader examplesLoader,
                     @Value("classpath:/prompts/ticket-analysis.st") Resource promptResource) {

        this.ticketService = ticketService;
        this.promptResource = promptResource;

        String fewShotInstruction = """
            Use the following examples strictly to understand the classification logic:
            ---------------------
            {question_answer_context}
            ---------------------
            CRITICAL: Do NOT copy the text from these examples into your response. 
            Analyze the user's target comment using this logic.
            """;

        this.chatClient = chatClientBuilder
            .defaultAdvisors(new QuestionAnswerAdvisor(
                examplesLoader.getVectorStore(),
                SearchRequest.defaults().withTopK(2),
                fewShotInstruction
            ))
            .build();
    }

    @Retryable(
        retryFor = {HttpServerErrorException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000, multiplier = 2.0)
    )
    public TicketDto getResponse(Comment comment) {
        log.info("Analyzing comment ID={}", comment.getId());
        try {
            TicketDto ticketDto = chatClient.prompt()
                .user(u -> u.text(promptResource)
                    .param("comment", comment.getContent()))
                .call()
                .entity(TicketDto.class);

            if (ticketDto == null) {
                log.info("Comment ID={} - AI returned null, NON-ACTIONABLE", comment.getId());
                return null;
            }

            if (ticketDto.getCategory() == null || ticketDto.getCategory().isBlank() ||
                ticketDto.getTitle() == null || ticketDto.getTitle().isBlank() ||
                ticketDto.getPriority() == null || ticketDto.getPriority().isBlank() ||
                ticketDto.getSummary() == null || ticketDto.getSummary().isBlank()) {

                log.info("Comment ID={} - incomplete data, NON-ACTIONABLE", comment.getId());
                return null;
            }

            log.info("Comment ID={} - ACTIONABLE, creating ticket: category={}, priority={}",
                comment.getId(), ticketDto.getCategory(), ticketDto.getPriority());

            ticketService.createTicket(ticketDto, comment);
            return ticketDto;

        } catch (HttpServerErrorException e) {
            log.error("Retryable error analyzing comment ID={}: {}", comment.getId(), e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Error analyzing comment ID={}: {}", comment.getId(), e.getMessage(), e);
            return null;
        }
    }
}