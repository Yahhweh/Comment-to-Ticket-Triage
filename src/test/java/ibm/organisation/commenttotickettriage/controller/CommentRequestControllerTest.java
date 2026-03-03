package ibm.organisation.commenttotickettriage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ibm.organisation.commenttotickettriage.service.CommentService;
import ibm.organisation.commenttotickettriage.service.ServiceException;
import ibm.organisation.commenttotickettriage.service.dto.CommentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentRequestController.class)
class CommentRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    void getComment_returnsOk_whenValidRequest() throws Exception {
        CommentDto validDto = getCommentDto();

        mockMvc.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
            .andExpect(status().isOk())
            .andExpect(content().string("Comment processed"));

        verify(commentService).processedComment(any(CommentDto.class));
    }

    @Test
    void getComment_returnsBadRequest_whenValidationFails() throws Exception {
        String invalidJson = "{}";

        mockMvc.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getComment_returnsInternalServerError_whenServiceThrowsException() throws Exception {
        CommentDto validDto = getCommentDto();

        String errorMessage = "Business logic failure";

        doThrow(new ServiceException(errorMessage))
            .when(commentService).processedComment(any(CommentDto.class));

        mockMvc.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string(errorMessage));
    }

    CommentDto getCommentDto(){
         CommentDto commentDto=  new CommentDto();
         commentDto.setContent("smth bad");
         return commentDto;
    }
}