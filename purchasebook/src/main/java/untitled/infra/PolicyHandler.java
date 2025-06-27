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
    PurchasedBookRepository purchasedBookRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BuyApproved'"
    )
    public void wheneverBuyApproved_PurchaseFinishAlert(
        @Payload BuyApproved buyApproved
    ) {
        BuyApproved event = buyApproved;
        System.out.println(
            "\n\n##### listener PurchaseFinishAlert : " + buyApproved + "\n\n"
        );

        // Sample Logic //
        PurchasedBook.purchaseFinishAlert(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BuyRejected'"
    )
    public void wheneverBuyRejected_PurchaseFailAlert(
        @Payload BuyRejected buyRejected
    ) {
        BuyRejected event = buyRejected;
        System.out.println(
            "\n\n##### listener PurchaseFailAlert : " + buyRejected + "\n\n"
        );

        // Sample Logic //
        PurchasedBook.purchaseFailAlert(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
