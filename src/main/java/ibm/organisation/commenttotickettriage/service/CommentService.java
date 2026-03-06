package ibm.organisation.commenttotickettriage.service;

import ibm.organisation.commenttotickettriage.entity.Comment;
import ibm.organisation.commenttotickettriage.service.dto.CommentDto;
import ibm.organisation.commenttotickettriage.service.dto.TicketDto;
import ibm.organisation.commenttotickettriage.repository.CommentRepository;
import ibm.organisation.commenttotickettriage.service.mapper.CommentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentService {
    private final CommentRepository repository;
    private final CommentMapper commentMapper;
    private final AiService aiService;

    public CommentService(CommentRepository repository, CommentMapper commentMapper,
                          AiService aiService) {
        this.repository = repository;
        this.commentMapper = commentMapper;
        this.aiService = aiService;
    }

    public long processComment(CommentDto commentDto) {
        log.info("Processing comment: {}", commentDto.getContent().substring(0, Math.min(50, commentDto.getContent().length())));

        Comment comment = commentMapper.toEntity(commentDto);
        repository.saveAndFlush(comment);

        TicketDto ticketDto = aiService.getResponse(comment);

        if (ticketDto != null) {
            log.info("Comment ID={} resulted in ticket creation", comment.getId());
        } else {
            log.info("Comment ID={} did not require a ticket (NON-ACTIONABLE or error)", comment.getId());
        }

        return comment.getId();
    }

    public List<CommentDto> getAllComments() {
        return repository.findAll().stream()
            .map(commentMapper::toDto)
            .collect(Collectors.toList());
    }

    public Page<CommentDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(commentMapper::toDto);
    }
}