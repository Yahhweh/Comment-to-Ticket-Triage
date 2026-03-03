package ibm.organisation.commenttotickettriage.service;

import ibm.organisation.commenttotickettriage.ai.client.CommentAiClient;
import ibm.organisation.commenttotickettriage.entity.Comment;
import ibm.organisation.commenttotickettriage.repository.CommentRepository;
import ibm.organisation.commenttotickettriage.service.dto.CommentDto;
import ibm.organisation.commenttotickettriage.service.mapper.CommentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentAiClient commentAiClient;

    @Mock
    private CommentRepository repository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    @Test
    void processedComment_mapsAndSavesEntity_whenCalled() {
        CommentDto dto = getDto();
        Comment expectedEntity = new Comment();

        when(commentMapper.toEntity(dto)).thenReturn(expectedEntity);

        commentService.processedComment(dto);

        verify(commentMapper).toEntity(dto);
        verify(repository).save(expectedEntity);
        verifyNoInteractions(commentAiClient);
    }

    CommentDto getDto(){
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("smth valid");
        return commentDto;
    }
}