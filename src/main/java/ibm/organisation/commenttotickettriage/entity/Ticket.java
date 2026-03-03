package ibm.organisation.commenttotickettriage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false, unique = true)
    @NotNull(message = "{ticket.comment.notnull}")
    private Comment comment;

    @Column(name = "title", nullable = false)
    @NotBlank(message = "{ticket.title.notblank}")
    private String title;

    @Column(name = "category", length = 50, nullable = false)
    @NotBlank(message = "{ticket.category.notblank}")
    private String category;

    @Column(name = "priority", length = 50, nullable = false)
    @NotBlank(message = "{ticket.priority.notblank}")
    private String priority;

    @Column(name = "summary", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "{ticket.summary.notblank}")
    private String summary;
}