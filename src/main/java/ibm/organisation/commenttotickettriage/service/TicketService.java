package ibm.organisation.commenttotickettriage.service;

import ibm.organisation.commenttotickettriage.entity.Comment;
import ibm.organisation.commenttotickettriage.entity.Ticket;
import ibm.organisation.commenttotickettriage.repository.TicketRepository;
import ibm.organisation.commenttotickettriage.service.dto.TicketDto;
import ibm.organisation.commenttotickettriage.service.mapper.TicketMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public TicketService(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    @Transactional(readOnly = true)
    public List<TicketDto> getAllTickets() {
        return ticketMapper.toDtoList(ticketRepository.findAll());
    }

    @Transactional(readOnly = true)
    public TicketDto getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Ticket with ID " + id + " not found"));
        return ticketMapper.toDto(ticket);
    }

    @Transactional
    public Ticket createTicket(TicketDto ticketDto, Comment comment) {
        Ticket ticket = ticketMapper.toEntity(ticketDto);
        ticket.setComment(comment);
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public Page<TicketDto> findAll(Pageable pageable){
        Page<TicketDto> tickets = ticketRepository.findAll(pageable).map(ticketMapper::toDto);;
        return tickets;
    }
}