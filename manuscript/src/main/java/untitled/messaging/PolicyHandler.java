package untitled.messaging;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import untitled.config.kafka.*;
import untitled.service.*; 
import untitled.controller.*; 
import untitled.domain.event.SummaryCreated;
import untitled.domain.event.CoverCreated;
import untitled.domain.event.BookRegistered;
import untitled.domain.event.WritingCompleted;


//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    private ManuscriptService manuscriptService;

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

        manuscriptService.applySummaryCreated(summaryCreated);
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

        manuscriptService.applyCoverCreated(coverCreated);
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

        manuscriptService.applyBookRegistered(bookRegistered);
    }

    
}
//>>> Clean Arch / Inbound Adaptor
