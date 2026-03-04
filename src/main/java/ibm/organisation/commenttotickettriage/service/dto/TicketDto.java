package ibm.organisation.commenttotickettriage.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    @Null
    private Long id;

    @NotBlank(message = "{ticket.title.notblank}")
    private String title;

    @NotBlank(message = "{ticket.category.notblank}")
    private String category;

    @NotBlank(message = "{ticket.priority.notblank}")
    private String priority;

    @NotBlank(message = "{ticket.summary.notblank}")
    private String summary;
}