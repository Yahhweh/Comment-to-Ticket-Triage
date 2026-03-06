package ibm.organisation.commenttotickettriage.service;

import ibm.organisation.commenttotickettriage.entity.Comment;
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
import org.springframework.web.client.HttpServerErrorException;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        Comment comment = createComment("I tried to refund subscription but it didn't work");
        TicketDto expectedTicketDto = createValidTicket();

        when(chatClient.prompt()
            .user(any(Consumer.class))
            .call()
            .entity(TicketDto.class)).thenReturn(expectedTicketDto);

        TicketDto result = aiService.getResponse(comment);

        assertThat(result).isEqualTo(expectedTicketDto);
        verify(ticketService).createTicket(expectedTicketDto, comment);
    }

    @Test
    void getResponse_whenAiReturnsNull_returnNullAndNoTicket() {
        Comment comment = createComment("I like chocolate");

        when(chatClient.prompt()
            .user(any(Consumer.class))
            .call()
            .entity(TicketDto.class))
            .thenReturn(null);

        TicketDto result = aiService.getResponse(comment);

        assertThat(result).isNull();
        verifyNoInteractions(ticketService);
    }

    @Test
    void getResponse_whenCategoryIsNull_returnNullAndNoTicket() {
        Comment comment = createComment("Some comment");
        TicketDto incompleteDto = new TicketDto();
        incompleteDto.setTitle("Title");
        incompleteDto.setCategory(null);
        incompleteDto.setPriority("high");
        incompleteDto.setSummary("Summary");

        when(chatClient.prompt()
            .user(any(Consumer.class))
            .call()
            .entity(TicketDto.class))
            .thenReturn(incompleteDto);

        TicketDto result = aiService.getResponse(comment);

        assertThat(result).isNull();
        verifyNoInteractions(ticketService);
    }

    @Test
    void getResponse_whenTitleIsBlank_returnNullAndNoTicket() {
        Comment comment = createComment("Some comment");
        TicketDto incompleteDto = new TicketDto();
        incompleteDto.setTitle("");
        incompleteDto.setCategory("bug");
        incompleteDto.setPriority("high");
        incompleteDto.setSummary("Summary");

        when(chatClient.prompt()
            .user(any(Consumer.class))
            .call()
            .entity(TicketDto.class))
            .thenReturn(incompleteDto);

        TicketDto result = aiService.getResponse(comment);

        assertThat(result).isNull();
        verifyNoInteractions(ticketService);
    }

    @Test
    void getResponse_whenPriorityIsNull_returnNullAndNoTicket() {
        Comment comment = createComment("Some comment");
        TicketDto incompleteDto = new TicketDto();
        incompleteDto.setTitle("Title");
        incompleteDto.setCategory("bug");
        incompleteDto.setPriority(null);
        incompleteDto.setSummary("Summary");

        when(chatClient.prompt()
            .user(any(Consumer.class))
            .call()
            .entity(TicketDto.class))
            .thenReturn(incompleteDto);

        TicketDto result = aiService.getResponse(comment);

        assertThat(result).isNull();
        verifyNoInteractions(ticketService);
    }

    @Test
    void getResponse_whenSummaryIsNull_returnNullAndNoTicket() {
        Comment comment = createComment("Some comment");
        TicketDto incompleteDto = new TicketDto();
        incompleteDto.setTitle("Title");
        incompleteDto.setCategory("bug");
        incompleteDto.setPriority("high");
        incompleteDto.setSummary(null);

        when(chatClient.prompt()
            .user(any(Consumer.class))
            .call()
            .entity(TicketDto.class))
            .thenReturn(incompleteDto);

        TicketDto result = aiService.getResponse(comment);

        assertThat(result).isNull();
        verifyNoInteractions(ticketService);
    }

    @Test
    void getResponse_whenServerError_throwsException() {
        Comment comment = createComment("Test");

        when(chatClient.prompt()
            .user(any(Consumer.class))
            .call()
            .entity(TicketDto.class))
            .thenThrow(new HttpServerErrorException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR));

        assertThatThrownBy(() -> aiService.getResponse(comment))
            .isInstanceOf(HttpServerErrorException.class);

        verifyNoInteractions(ticketService);
    }

    @Test
    void getResponse_whenUnexpectedException_returnNull() {
        Comment comment = createComment("Test");

        when(chatClient.prompt()
            .user(any(Consumer.class))
            .call()
            .entity(TicketDto.class))
            .thenThrow(new RuntimeException("Unexpected error"));

        TicketDto result = aiService.getResponse(comment);

        assertThat(result).isNull();
        verifyNoInteractions(ticketService);
    }

    private Comment createComment(String content) {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent(content);
        return comment;
    }

    private TicketDto createValidTicket() {
        TicketDto dto = new TicketDto();
        dto.setId(1L);
        dto.setCommentId(1L);
        dto.setTitle("Problems with refund");
        dto.setCategory("billing");
        dto.setPriority("high");
        dto.setSummary("Customer unable to process refund");
        return dto;
    }
}