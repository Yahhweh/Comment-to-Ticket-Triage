package ibm.organisation.commenttotickettriage.service.examples;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@Getter
public class ExamplesLoader {

    @Value("classpath:prompts/examples/examples.txt")
    private Resource txtFile;
    public final VectorStore vectorStore;

    public ExamplesLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadExamples() throws Exception {
        String content = txtFile.getContentAsString(StandardCharsets.UTF_8);
        String[] parts = content.split("===");

        List<Document> docs = Arrays.stream(parts)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(Document::new)
            .toList();

        vectorStore.add(docs);
    }

}
