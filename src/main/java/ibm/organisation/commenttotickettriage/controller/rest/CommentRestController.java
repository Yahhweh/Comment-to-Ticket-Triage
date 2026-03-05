package ibm.organisation.commenttotickettriage.controller.rest;

import lombok.extern.slf4j.Slf4j;
import ibm.organisation.commenttotickettriage.service.dto.CommentDto;
import ibm.organisation.commenttotickettriage.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comments")
public class CommentRestController {

    private final CommentService service;

    public CommentRestController(CommentService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<String> createComment(@RequestBody @Valid CommentDto commentDto) {
        log.info("Received request to create comment. content: {}", commentDto.getContent());
        Long id = service.processComment(commentDto);
        log.debug("Comment saved id: {}", id);
        return ResponseEntity.ok("Comment processed");
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        log.info("Received request to get all comments");
        List<CommentDto> comments = service.getAllComments();
        log.debug("Fetched {} tickets", comments.size());
        return ResponseEntity.ok(comments);
    }
}
