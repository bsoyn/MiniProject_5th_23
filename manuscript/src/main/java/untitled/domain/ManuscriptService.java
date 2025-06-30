package untitled.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManuscriptService {

    private final ManuscriptRepository manuscriptRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void requestPublication(RequestPublicationCommand command) {
        Manuscript manuscript = new Manuscript();
        manuscript.setAuthorId(command.getAuthorId());
        manuscript.setTitle(command.getTitle());
        manuscript.setContent(command.getContent());

        manuscriptRepository.save(manuscript);

        PublicationRequested event = new PublicationRequested(manuscript);
        kafkaTemplate.send("book.completed", event); 
    }
}
