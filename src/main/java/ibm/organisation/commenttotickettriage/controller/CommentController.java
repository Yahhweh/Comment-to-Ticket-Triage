package ibm.organisation.commenttotickettriage.controller;

import ibm.organisation.commenttotickettriage.service.CommentService;
import ibm.organisation.commenttotickettriage.service.dto.CommentDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("ui/comment")
public class CommentController {

    CommentService service;

    public CommentController(CommentService commentService) {
        this.service = commentService;
    }

    @PostMapping
    public String postComment(
        @ModelAttribute("comment") @Valid CommentDto commentDto, BindingResult bindingResult) {
        log.info("Received request to post comment");
        if (bindingResult.hasErrors()) {
            return "failed";
        } else {
            service.processComment(commentDto);
            return "success";
        }
    }

    @GetMapping
    public String getComment(Model model) {
        log.info("Received request to fetch comment");
        model.addAttribute("comment", new CommentDto());
        return "comments";
    }


    @GetMapping("/all")
    public String getAllComments(Model model,
                                 @PageableDefault(size = 3) Pageable pageable) {
        log.info("Received request to fetch all comments");
        Page<CommentDto> page = service.findAll(pageable);

        model.addAttribute("comments", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "all");

        return "showComments";
    }


}
