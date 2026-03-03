package ibm.organisation.commenttotickettriage.service.mapper;

import ibm.organisation.commenttotickettriage.entity.Comment;
import ibm.organisation.commenttotickettriage.service.dto.CommentDto;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Mapper(
    unmappedTargetPolicy =ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = "spring",
    imports = {LocalDateTime.class}
)
public interface CommentMapper {

    @Mapping(target = "timestamp", expression = "java(LocalDateTime.now())")
    @Mapping(target = "processed", constant = "false")
    @Mapping(target = "content", source = "content")
    Comment toEntity(CommentDto dto);

}
