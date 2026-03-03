package ibm.organisation.commenttotickettriage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    public Comment(String content, LocalDateTime timestamp, boolean processed) {
        this.content = content;
        this.timestamp = timestamp;
        this.processed = processed;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean processed = false;
}
