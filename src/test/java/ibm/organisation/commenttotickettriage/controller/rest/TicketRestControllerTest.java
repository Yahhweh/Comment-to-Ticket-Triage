package ibm.organisation.commenttotickettriage.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ibm.organisation.commenttotickettriage.service.TicketService;
import ibm.organisation.commenttotickettriage.service.dto.TicketDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketRestController.class)
class TicketRestControllerTest {

    private static final String BASE_URL = "/tickets";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService ticketService;

    @Test
    void getAllTickets_whenCalled_returnOkAndTicketList() throws Exception {
        TicketDto ticketDto = createTicketDto();
        List<TicketDto> tickets = List.of(ticketDto);

        when(ticketService.getAllTickets()).thenReturn(tickets);

        mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(tickets)));

        verify(ticketService).getAllTickets();
    }

    @Test
    void getTicketById_whenValidId_returnOkAndTicket() throws Exception {
        Long ticketId = 1L;
        TicketDto ticketDto = createTicketDto();

        when(ticketService.getTicketById(ticketId)).thenReturn(ticketDto);

        mockMvc.perform(get(BASE_URL + "/{id}", ticketId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(ticketDto)));

        verify(ticketService).getTicketById(ticketId);
    }

    private TicketDto createTicketDto() {
        TicketDto dto = new TicketDto();
        dto.setTitle("Database Connection Issue");
        dto.setCategory("bug");
        dto.setPriority("high");
        dto.setSummary("Application fails to connect to the database on startup");
        dto.setCommentId(10L);
        return dto;
    }
}