    package ibm.organisation.commenttotickettriage.service;

    import ibm.organisation.commenttotickettriage.entity.Comment;
    import ibm.organisation.commenttotickettriage.service.dto.CommentDto;
    import ibm.organisation.commenttotickettriage.repository.CommentRepository;
    import ibm.organisation.commenttotickettriage.service.mapper.CommentMapper;
    import jakarta.persistence.EntityNotFoundException;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

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

        @Transactional
        public long processComment(CommentDto commentDto) {
            Comment comment = commentMapper.toEntity(commentDto);
            repository.saveAndFlush(comment);
            aiService.getResponse(comment);

            return comment.getId();
        }
    }