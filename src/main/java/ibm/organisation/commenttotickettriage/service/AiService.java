package ibm.organisation.commenttotickettriage.service;

import ibm.organisation.commenttotickettriage.entity.Comment;
import ibm.organisation.commenttotickettriage.service.dto.TicketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);

    private final ChatClient chatClient;
    private final TicketService ticketService;

    public AiService(ChatClient.Builder chatClient, TicketService ticketService) {
        this.chatClient = chatClient.build();
        this.ticketService = ticketService;
    }

    public TicketDto getResponse(Comment comment) {

        String prompt =
            "### PRIMARY DIRECTIVE (FAIL-FAST)\n" +
                "Analyze PulseDesk user comment: {comment}.\n" +
                "If the text is hate speech, racism, undirected toxicity, spam, or meaningless text -> OUTPUT EXACTLY \\{\\} AND STOP GENERATION.\n\n" +
                "### VALIDATION CONDITIONS\n" +
                "Generate a full JSON object ONLY if the comment contains:\n" +
                "- A legitimate technical issue, bug report, feature request, or BILLING/PAYMENT problem.\n" +
                "- Negative feedback or insults DIRECTLY targeting the company or product.\n\n" +
                "### JSON SCHEMA (FOR VALID COMMENTS ONLY)\n" +
                "\\{\n" +
                "  \"title\": \"Technical SLUG. Max 5 words.\",\n" +
                "  \"summary\": \"Detailed narrative. Min 15 words. MUST NOT equal title.\",\n" +
                "  \"category\": \"One of: [bug, feature, billing, account, other]\",\n" +
                "  \"priority\": \"One of: [low, medium, high]\"\n" +
                "\\}\n\n" +
                "Respond ONLY with valid JSON.";

        TicketDto ticketDto = chatClient.prompt()
            .user(u -> u.text(prompt)
                .param("comment", comment.getContent()))
            .call()
            .entity(TicketDto.class);

        if (ticketDto == null || ticketDto.getCategory() == null) {
            log.info("Comment ID {} evaluated as non-actionable. Ticket creation skipped.", comment.getId());
            return ticketDto;
        }

        ticketService.createTicket(ticketDto);

        return ticketDto;
    }
}