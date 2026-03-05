package ibm.organisation.commenttotickettriage.controller.rest;

import ibm.organisation.commenttotickettriage.service.TicketService;
import ibm.organisation.commenttotickettriage.service.dto.TicketDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tickets")
public class TicketRestController {

    private final TicketService ticketService;

    public TicketRestController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> getAllTickets() {
        log.info("Received request to get all tickets");
        List<TicketDto> tickets = ticketService.getAllTickets();
        log.debug("Fetched {} tickets", tickets.size());
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable("id") Long id) {
        log.info("Received request to fetch ticket by id: {}", id);
        TicketDto ticket = ticketService.getTicketById(id);
        log.debug("Fetched ticket with id: {}", id);
        return ResponseEntity.ok(ticket);
    }
}