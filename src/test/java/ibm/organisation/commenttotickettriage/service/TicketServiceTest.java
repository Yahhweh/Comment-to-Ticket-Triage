package ibm.organisation.commenttotickettriage.service;

import ibm.organisation.commenttotickettriage.entity.Comment;
import ibm.organisation.commenttotickettriage.entity.Ticket;
import ibm.organisation.commenttotickettriage.repository.TicketRepository;
import ibm.organisation.commenttotickettriage.service.dto.TicketDto;
import ibm.organisation.commenttotickettriage.service.mapper.TicketMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void createTicket_savesAndReturnsTicket() {
        TicketDto dto = new TicketDto();
        Comment comment = new Comment();
        comment.setId(1L);
        Ticket entity = new Ticket();

        when(ticketMapper.toEntity(dto)).thenReturn(entity);
        when(ticketRepository.save(entity)).thenReturn(entity);

        Ticket result = ticketService.createTicket(dto, comment);

        assertEquals(entity, result);
        assertEquals(comment, entity.getComment());
        verify(ticketRepository).save(entity);
    }

    @Test
    void getAllTickets_returnsList_whenTicketsExist() {
        List<Ticket> entities = List.of(new Ticket());
        List<TicketDto> dtos = List.of(new TicketDto());

        when(ticketRepository.findAll()).thenReturn(entities);
        when(ticketMapper.toDtoList(entities)).thenReturn(dtos);

        List<TicketDto> result = ticketService.getAllTickets();

        assertThat(result).hasSize(1).isEqualTo(dtos);
        verify(ticketRepository).findAll();
        verify(ticketMapper).toDtoList(entities);
    }

    @Test
    void getTicketById_returnsTicket_whenExists() {
        Long id = 1L;
        Ticket entity = new Ticket();
        TicketDto dto = getTicketDto();

        when(ticketRepository.findById(id)).thenReturn(Optional.of(entity));
        when(ticketMapper.toDto(entity)).thenReturn(dto);

        TicketDto result = ticketService.getTicketById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        verify(ticketRepository).findById(id);
    }

    @Test
    void getTicketById_throwsException_whenNotExists() {
        Long id = 99L;

        when(ticketRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketById(id))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Ticket with ID " + id + " not found");

        verify(ticketRepository).findById(id);
        verify(ticketMapper, never()).toDto(any());
    }

    TicketDto getTicketDto(){
        return new TicketDto(1L,1L, "Title", "Bug", "High", "Summary");
    }
}