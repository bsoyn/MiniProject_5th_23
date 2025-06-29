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
    SubscribeRepository subscribeRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BuyApproved'"
    )
    public void wheneverBuyApproved_SubscribeFinish(
        @Payload BuyApproved buyApproved
    ) {
        BuyApproved event = buyApproved;
        System.out.println(
            "\n\n##### listener SubscribeFinish : " + buyApproved + "\n\n"
        );

        // Sample Logic //
        Subscribe.subscribeFinish(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BuyRejected'"
    )
    public void wheneverBuyRejected_SubscribeFailAlert(
        @Payload BuyRejected buyRejected
    ) {
        BuyRejected event = buyRejected;
        System.out.println(
            "\n\n##### listener SubscribeFailAlert : " + buyRejected + "\n\n"
        );

        // Sample Logic //
        Subscribe.subscribeFailAlert(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BoolAccessRequested'"
    )
    public void wheneverBoolAccessRequested_SubscriptionValidCheck(
        @Payload BoolAccessRequested boolAccessRequested
    ) {
        BoolAccessRequested event = boolAccessRequested;
        System.out.println(
            "\n\n##### listener SubscriptionValidCheck : " +
            boolAccessRequested +
            "\n\n"
        );

        // Sample Logic //
        Subscribe.subscriptionValidCheck(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
