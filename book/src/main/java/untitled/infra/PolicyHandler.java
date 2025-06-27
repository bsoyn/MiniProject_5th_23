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
    BookRepository bookRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriptionValidChecked'"
    )
    public void wheneverSubscriptionValidChecked_BookAccessApproveAlert(
        @Payload SubscriptionValidChecked subscriptionValidChecked
    ) {
        SubscriptionValidChecked event = subscriptionValidChecked;
        System.out.println(
            "\n\n##### listener BookAccessApproveAlert : " +
            subscriptionValidChecked +
            "\n\n"
        );

        // Sample Logic //
        Book.bookAccessApproveAlert(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PurchaseBookConfirmed'"
    )
    public void wheneverPurchaseBookConfirmed_BookAccessApproveAlert(
        @Payload PurchaseBookConfirmed purchaseBookConfirmed
    ) {
        PurchaseBookConfirmed event = purchaseBookConfirmed;
        System.out.println(
            "\n\n##### listener BookAccessApproveAlert : " +
            purchaseBookConfirmed +
            "\n\n"
        );

        // Sample Logic //
        Book.bookAccessApproveAlert(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriptionFinished'"
    )
    public void wheneverSubscriptionFinished_BookAccessDeniedAlert(
        @Payload SubscriptionFinished subscriptionFinished
    ) {
        SubscriptionFinished event = subscriptionFinished;
        System.out.println(
            "\n\n##### listener BookAccessDeniedAlert : " +
            subscriptionFinished +
            "\n\n"
        );

        // Sample Logic //
        Book.bookAccessDeniedAlert(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='NotParchaseBookConfirmed'"
    )
    public void wheneverNotParchaseBookConfirmed_BookAccessDeniedAlert(
        @Payload NotParchaseBookConfirmed notParchaseBookConfirmed
    ) {
        NotParchaseBookConfirmed event = notParchaseBookConfirmed;
        System.out.println(
            "\n\n##### listener BookAccessDeniedAlert : " +
            notParchaseBookConfirmed +
            "\n\n"
        );

        // Sample Logic //
        Book.bookAccessDeniedAlert(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='WritingCompleted'"
    )
    public void wheneverWritingCompleted_RequestBookRegistrationAlert(
        @Payload WritingCompleted writingCompleted
    ) {
        WritingCompleted event = writingCompleted;
        System.out.println(
            "\n\n##### listener RequestBookRegistrationAlert : " +
            writingCompleted +
            "\n\n"
        );

        // Sample Logic //
        Book.requestBookRegistrationAlert(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
