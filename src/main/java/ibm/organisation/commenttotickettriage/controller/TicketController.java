package ibm.organisation.commenttotickettriage.controller;

import ibm.organisation.commenttotickettriage.entity.Ticket;
import ibm.organisation.commenttotickettriage.service.TicketService;
import ibm.organisation.commenttotickettriage.service.dto.TicketDto;
import ibm.organisation.commenttotickettriage.service.mapper.TicketMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TicketController {

    TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping(value = "/tickets")
    public String getTickets(Model model,
                             @PageableDefault(size = 3)Pageable pageable){

        Page<TicketDto> page = service.findAll(pageable);

        model.addAttribute("tickets", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "tickets");

        return "tickets";
    }
}
