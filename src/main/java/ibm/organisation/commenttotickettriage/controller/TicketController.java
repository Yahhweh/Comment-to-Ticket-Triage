package ibm.organisation.commenttotickettriage.controller;

import ibm.organisation.commenttotickettriage.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class TicketController {

    AiService service;

    public TicketController(AiService controller) {
        this.service = controller;
    }

    @PostMapping(value = "/ai")
    ResponseEntity<String> response(@RequestBody String message){
        String ans = service.getResponse(message);
        return ResponseEntity.ok().body(ans);
    }

}
