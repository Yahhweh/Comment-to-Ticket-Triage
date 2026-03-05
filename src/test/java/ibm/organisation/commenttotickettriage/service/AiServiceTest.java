package ibm.organisation.commenttotickettriage.service;

import ibm.organisation.commenttotickettriage.entity.Comment;
import ibm.organisation.commenttotickettriage.entity.Ticket;
import ibm.organisation.commenttotickettriage.service.dto.TicketDto;
import ibm.organisation.commenttotickettriage.service.examples.ExamplesLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiServiceTest {
    @Mock
    private ChatClient.Builder chatClientBuilder;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;
    @Mock
    private TicketService ticketService;
    @Mock
    private ExamplesLoader examplesLoader;
    @Mock
    private Resource resource;
    @Mock
    private VectorStore vectorStore;
    private AiService aiService;

    @BeforeEach
    void setUp() {
        when(examplesLoader.getVectorStore()).thenReturn(vectorStore);
        when(chatClientBuilder.defaultAdvisors(any(QuestionAnswerAdvisor.class))).thenReturn(chatClientBuilder);
        when(chatClientBuilder.build()).thenReturn(chatClient);

        aiService = new AiService(chatClientBuilder, ticketService, examplesLoader, resource);
    }

    @Test
    void getResponse_whenValidTicketDto_returnTicketDtoAndCreateTicket() {
        Comment comment = getValidComment();
        TicketDto expectedTicketDto = getTicket();

        when(chatClient.prompt()
            .user(any(Consumer.class))
            .call()
            .entity(TicketDto.class)).thenReturn(expectedTicketDto);

        TicketDto ticketDto = aiService.getResponse(comment);
        assertThat(ticketDto).isEqualTo(expectedTicketDto);
        verify(ticketService).createTicket(expectedTicketDto, comment);
    }

    @Test
    void getResponse_whenCategoryIsNull_returnTicketDtoAndDoNotCreateTicket() {
        Comment comment = getInvalidComment();

        TicketDto expectedDto = new TicketDto();
        expectedDto.setCategory(null);

        when(chatClient.prompt()
            .user(any(Consumer.class))
            .call()
            .entity(TicketDto.class))
            .thenReturn(expectedDto);

        TicketDto actualDto = aiService.getResponse(comment);

        assertEquals(expectedDto, actualDto);
        verifyNoInteractions(ticketService);
    }

    Comment getValidComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("I tried to refund subscription but it didnt word and I still wait my money");
        return comment;
    }

    Comment getInvalidComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("I like chocolate");
        return comment;
    }

    TicketDto getTicket() {
        return new TicketDto(1L, 1L, "Problems with refund", "billing", "high", "smth here");
    }
}