package ibm.organisation.commenttotickettriage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@EnableRetry
public class CommentToTicketTriageApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommentToTicketTriageApplication.class, args);
    }

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
