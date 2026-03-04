package ibm.organisation.commenttotickettriage.service.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    @Null
    private Long id;

    @NotBlank(message = "{comment.content.notblank}")
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9]+\\s+[A-Za-z0-9]+(\\s+[A-Za-z0-9]+)*$",
        message = "{comment.content.regexp}")
    private String content;
}
