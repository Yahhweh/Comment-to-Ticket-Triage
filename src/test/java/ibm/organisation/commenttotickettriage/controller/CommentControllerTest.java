package ibm.organisation.commenttotickettriage.controller;

import ibm.organisation.commenttotickettriage.service.CommentService;
import ibm.organisation.commenttotickettriage.service.dto.CommentDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    private static final String BASE_URL = "/comment";
    private static final String VIEW_COMMENTS = "comments";
    private static final String VIEW_SUCCESS = "success";
    private static final String VIEW_FAILED = "failed";
    private static final String MODEL_ATTRIBUTE = "comment";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    void getComment_whenCalled_returnCommentsView() throws Exception {
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(view().name(VIEW_COMMENTS))
            .andExpect(model().attributeExists(MODEL_ATTRIBUTE));
    }

    @Test
    void postComment_whenValidInput_returnSuccessView() throws Exception {
        String validContent = "smth valid here";

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", validContent))
            .andExpect(status().isOk())
            .andExpect(model().hasNoErrors())
            .andExpect(view().name(VIEW_SUCCESS));

        verify(commentService).processComment(any(CommentDto.class));
    }

    @Test
    void postComment_whenInvalidInput_returnFailedView() throws Exception {
        String invalidContent = "";

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", invalidContent))
            .andExpect(status().isOk())
            .andExpect(model().hasErrors())
            .andExpect(model().attributeHasFieldErrors(MODEL_ATTRIBUTE, "content"))
            .andExpect(view().name(VIEW_FAILED));

        verifyNoInteractions(commentService);
    }
}