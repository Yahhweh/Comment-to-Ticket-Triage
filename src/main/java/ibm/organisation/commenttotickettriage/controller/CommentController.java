package ibm.organisation.commenttotickettriage.controller;

import ibm.organisation.commenttotickettriage.service.CommentService;
import ibm.organisation.commenttotickettriage.service.dto.CommentDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comment")
public class CommentController {

    CommentService service;

    public CommentController(CommentService commentService) {
        this.service = commentService;
    }

    @PostMapping
    public String postComment(
        @ModelAttribute("comment") @Valid CommentDto commentDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "failed";
        }else {
            service.processComment(commentDto);
            return "success";
        }
    }

    @GetMapping
    public String getComment(Model model){
        model.addAttribute("comment", new CommentDto());
        return "comments";
    }
}
