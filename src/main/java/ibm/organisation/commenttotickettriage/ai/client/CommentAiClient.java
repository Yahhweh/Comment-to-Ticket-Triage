package ibm.organisation.commenttotickettriage.ai.client;

import ibm.organisation.commenttotickettriage.entity.Comment;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class CommentAiClient {

    private final ChatModel chatModel;

    public CommentAiClient(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String generateResponse(Comment comment) {
        String prompt = String.format(
            "### TASK\n" +
                "Analyze user comment.\n\n" +
                "### FIELD DIFFERENTIATION RULES\n" +
                "1. 'title': A short, technical SLUG or SUBJECT line. Max 5 words. (Example: 'Login Page 404 Error')\n" +
                "2. 'summary': A detailed NARRATIVE of the specific problem. Min 15 words. (Example: 'The user is reporting a persistent 404 error when attempting to access the authentication module via the primary dashboard link.')\n" +
                "3. CONSTRAINT: 'title' and 'summary' MUST NOT be identical. 'title' is a headline; 'summary' is a technical description.\n\n" +
                "### CLASSIFICATION\n" +
                "- isTicket: true if issue/bug/request, false if compliment.\n" +
                "- category: [bug, feature, billing, account, other].\n" +
                "- priority: [low, medium, high].\n\n" +
                "### OUTPUT FORMAT\n" +
                "  \"isTicket\": boolean, " +
                "  \"title\": \"string\", " +
                "  \"category\": \"string\", " +
                "  \"priority\": \"string\", " +
                "  \"summary\": \"string\" " +
                "\"%s\"",
            comment.getContent()
        );

        ChatResponse chatResponse = chatModel.call(new Prompt(prompt));
        return chatResponse.getResult().getOutput().getContent();
    }
}
