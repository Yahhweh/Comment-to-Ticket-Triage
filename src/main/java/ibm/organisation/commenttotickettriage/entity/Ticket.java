package ibm.organisation.commenttotickettriage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", referencedColumnName = "id", unique = true)
    private Comment comment;

    @Column(name = "category", length = 50, nullable = false)
    private String category;

    @Column(name = "priority", length = 50, nullable = false)
    private String priority;

    @Column(name = "summary", columnDefinition = "TEXT", nullable = false)
    private String summary;
}