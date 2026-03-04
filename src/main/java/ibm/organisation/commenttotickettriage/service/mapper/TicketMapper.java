package ibm.organisation.commenttotickettriage.service.mapper;

import ibm.organisation.commenttotickettriage.entity.Ticket;
import ibm.organisation.commenttotickettriage.service.dto.TicketDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = "spring"
)
public interface TicketMapper {

    @Mapping(target = "comment", ignore = true)
    Ticket toEntity(TicketDto dto);

    @Mapping(source = "comment.id", target = "commentId")
    TicketDto toDto(Ticket entity);

    List<TicketDto> toDtoList(List<Ticket> entities);
}