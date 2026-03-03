package ibm.organisation.commenttotickettriage.repository;

import ibm.organisation.commenttotickettriage.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
