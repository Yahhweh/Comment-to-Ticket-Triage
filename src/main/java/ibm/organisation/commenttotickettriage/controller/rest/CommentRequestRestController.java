package ibm.organisation.commenttotickettriage.controller.rest;

import ibm.organisation.commenttotickettriage.service.dto.CommentDto;
import ibm.organisation.commenttotickettriage.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class CommentRequestRestController {

    private final CommentService service;

    public CommentRequestRestController(CommentService service) {
        this.service = service;
    }

    @PostMapping(value = "/comments/api")
    public ResponseEntity<String> createComment(@RequestBody @Valid CommentDto commentDto){
        Long id = service.processComment(commentDto);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();

        return ResponseEntity.created(location).body("Comment processed");
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<CommentDto> comments = service.getAllComments();
        return ResponseEntity.ok(comments);
    }
}
