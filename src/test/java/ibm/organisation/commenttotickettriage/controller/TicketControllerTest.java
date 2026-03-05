package ibm.organisation.commenttotickettriage.controller;

import ibm.organisation.commenttotickettriage.service.TicketService;
import ibm.organisation.commenttotickettriage.service.dto.TicketDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    private static final String BASE_URL = "/ui/ticket/all";
    private static final String VIEW_TICKETS = "tickets";
    private static final String MODEL_ATTRIBUTE_TICKETS = "tickets";
    private static final String MODEL_ATTRIBUTE_PAGE = "page";
    private static final String MODEL_ATTRIBUTE_URL = "url";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Test
    void getTickets_whenCalled_returnTicketsView() throws Exception {
        Page<TicketDto> page = new PageImpl<>(Collections.emptyList());
        when(ticketService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(view().name(VIEW_TICKETS))
            .andExpect(model().attributeExists(MODEL_ATTRIBUTE_TICKETS))
            .andExpect(model().attributeExists(MODEL_ATTRIBUTE_PAGE))
            .andExpect(model().attributeExists(MODEL_ATTRIBUTE_URL));

        verify(ticketService).findAll(any(Pageable.class));
    }
}