package ibm.organisation.commenttotickettriage.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    @Null
    private Long id;

    @NotBlank(message = "{comment.content.notblank}")
    @Size(min = 4, max = 100, message = "{comment.content.size}")
    private String content;
}