package ibm.organisation.commenttotickettriage.controller;

import ibm.organisation.commenttotickettriage.service.dto.CommentDto;
import ibm.organisation.commenttotickettriage.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentRequestController {

    CommentService service;

    public CommentRequestController(CommentService service) {
        this.service = service;
    }

    @PostMapping(value = "/comments")
    public ResponseEntity<String> getComment(@RequestBody @Valid CommentDto commentDto){
        service.processedComment(commentDto);
        return ResponseEntity.ok("Comment processed");
    }
}
