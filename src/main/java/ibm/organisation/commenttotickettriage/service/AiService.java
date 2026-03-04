package ibm.organisation.commenttotickettriage.service;

import ibm.organisation.commenttotickettriage.entity.Comment;
import ibm.organisation.commenttotickettriage.service.dto.TicketDto;
import ibm.organisation.commenttotickettriage.service.examples.ExamplesLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class AiService {

    private final ChatClient chatClient;
    private final TicketService ticketService;
    private final Resource promptResource;
    private final ExamplesLoader examplesLoader;

    public AiService(ChatClient.Builder chatClientBuilder,
                     TicketService ticketService,
                     ExamplesLoader examplesLoader,
                     @Value("classpath:/prompts/ticket-analysis.st") Resource promptResource) {

        this.ticketService = ticketService;
        this.examplesLoader = examplesLoader;
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
        retryFor = { RuntimeException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000, multiplier = 2.0)
    )
    public TicketDto getResponse(Comment comment) {
        TicketDto ticketDto = chatClient.prompt()
            .user(u -> u.text(promptResource)
                .param("comment", comment.getContent()))
            .call()
            .entity(TicketDto.class);

        if (ticketDto == null || ticketDto.getCategory() == null) {
            return ticketDto;
        }
        ticketService.createTicket(ticketDto, comment);
        return ticketDto;
    }
}