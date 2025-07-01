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
import untitled.service.PurchaseService;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    PurchaseService purchaseService;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    // '구매 완료 알람' policy - 포인트 BC에서 발행한 'BuyApproved' 이벤트 수신
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
        purchaseService.handleBuyApproved(event);

        // Sample Logic //
        //PurchasedBook.purchaseFinishAlert(event);
    }

    // '구매 실패 알람' policy - 포인트 BC에서 발행한 'BuyRejected' 이벤트 수신
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
        purchaseService.handleBuyRejected(event);

        // Sample Logic //
        //PurchasedBook.purchaseFailAlert(event);
    }

    // '구매한 도서인지 확인' policy - 도서 관리 BC에서 발행한 'BookAccessRequested' 이벤트 수신
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookAccessRequested'"
    )
    public void wheneverBookAccessRequested_CheckOwnership(@Payload BookAccessRequested event) {
        System.out.println("\n##### listener CheckOwnership : " + event + "\n");

        purchaseService.handleBookAccessRequest(event);
    }

    // '구매 유도 알림' policy - 도서 관리 BC에서 발행한 'BookAccessRejected' 이벤트 수신
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookAccessRejected'"
    )
    public void wheneverBookAccessRejected_SuggestPurchase(
        @Payload BookAccessRejected event
    ) {
        System.out.println("\n##### listener SuggestPurchase : " + event + "\n");
        
        purchaseService.handleSuggestPurchase(event);
    }

}
//>>> Clean Arch / Inbound Adaptor
