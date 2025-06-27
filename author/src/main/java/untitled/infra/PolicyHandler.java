package untitled.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import untitled.config.kafka.KafkaProcessor;
import untitled.domain.*;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    AuthorRepository authorRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='AuthorDenied'"
    )
    public void wheneverAuthorDenied_SendRejectAlert(
        @Payload AuthorDenied authorDenied
    ) {
        AuthorDenied event = authorDenied;
        System.out.println(
            "\n\n##### listener SendRejectAlert : " + authorDenied + "\n\n"
        );

        // Sample Logic //
        Author.sendRejectAlert(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='AuthorApproved'"
    )
    public void wheneverAuthorApproved_UpdateApprovalStatus(
        @Payload AuthorApproved authorApproved
    ) {
        AuthorApproved event = authorApproved;
        System.out.println(
            "\n\n##### listener UpdateApprovalStatus : " +
            authorApproved +
            "\n\n"
        );

        // Sample Logic //
        Author.updateApprovalStatus(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
