package ibm.organisation.commenttotickettriage.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ibm.organisation.commenttotickettriage.service.CommentService;
import ibm.organisation.commenttotickettriage.service.dto.CommentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentRestController.class)
class CommentRestControllerTest {

    private static final String BASE_URL = "/comments";
    private static final String SUCCESS_RESPONSE = "Comment processed";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    void createComment_whenValidRequest_returnCreated() throws Exception {
        CommentDto validDto = createCommentDto();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
            .andExpect(status().isOk())
            .andExpect(content().string(SUCCESS_RESPONSE));

        verify(commentService).processComment(any(CommentDto.class));
    }

    @Test
    void createComment_whenValidationFails_returnBadRequest() throws Exception {
        String invalidJson = "{}";

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());

        verifyNoInteractions(commentService);
    }

    private CommentDto createCommentDto() {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("smth bad");
        return commentDto;
    }
}