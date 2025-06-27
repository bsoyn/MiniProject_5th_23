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
    ManuscriptRepository manuscriptRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SummaryCreated'"
    )
    public void wheneverSummaryCreated_AlertSummaryCreated(
        @Payload SummaryCreated summaryCreated
    ) {
        SummaryCreated event = summaryCreated;
        System.out.println(
            "\n\n##### listener AlertSummaryCreated : " +
            summaryCreated +
            "\n\n"
        );

        // Sample Logic //
        Manuscript.alertSummaryCreated(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='CoverCreated'"
    )
    public void wheneverCoverCreated_AlertCoverCreated(
        @Payload CoverCreated coverCreated
    ) {
        CoverCreated event = coverCreated;
        System.out.println(
            "\n\n##### listener AlertCoverCreated : " + coverCreated + "\n\n"
        );

        // Sample Logic //
        Manuscript.alertCoverCreated(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookRegistered'"
    )
    public void wheneverBookRegistered_AlertBookRegistration(
        @Payload BookRegistered bookRegistered
    ) {
        BookRegistered event = bookRegistered;
        System.out.println(
            "\n\n##### listener AlertBookRegistration : " +
            bookRegistered +
            "\n\n"
        );

        // Sample Logic //
        Manuscript.alertBookRegistration(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
