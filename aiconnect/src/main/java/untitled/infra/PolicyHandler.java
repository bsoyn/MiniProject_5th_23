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
    BookCoverRepository bookCoverRepository;

    @Autowired
    BookSummaryRepository bookSummaryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SummaryCreated'"
    )
    public void wheneverSummaryCreated_CreateCover(
        @Payload SummaryCreated summaryCreated
    ) {
        SummaryCreated event = summaryCreated;
        System.out.println(
            "\n\n##### listener CreateCover : " + summaryCreated + "\n\n"
        );
        // Sample Logic //

    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PublicationRequested'"
    )
    public void wheneverPublicationRequested_SummaryBook(
        @Payload PublicationRequested publicationRequested
    ) {
        PublicationRequested event = publicationRequested;
        System.out.println(
            "\n\n##### listener SummaryBook : " + publicationRequested + "\n\n"
        );

        // Sample Logic //
        BookSummary.summaryBook(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
