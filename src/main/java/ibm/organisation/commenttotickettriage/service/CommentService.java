package ibm.organisation.commenttotickettriage.service;

import ibm.organisation.commenttotickettriage.ai.client.CommentAiClient;
import ibm.organisation.commenttotickettriage.entity.Comment;
import ibm.organisation.commenttotickettriage.service.dto.CommentDto;
import ibm.organisation.commenttotickettriage.repository.CommentRepository;
import ibm.organisation.commenttotickettriage.service.mapper.CommentMapper;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentAiClient commentAiClient;
    private final CommentRepository repository;
    private final CommentMapper commentMapper;

    public CommentService(CommentAiClient commentAiClient, CommentRepository repository,
                          CommentMapper commentMapper) {
        this.commentAiClient = commentAiClient;
        this.repository = repository;
        this.commentMapper = commentMapper;
    }

    public void processedComment(CommentDto commentDto){
        Comment comment = commentMapper.toEntity(commentDto);
        repository.save(comment);
    }
}
