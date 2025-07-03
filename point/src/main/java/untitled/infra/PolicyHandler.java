package untitled.infra;
 
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    PointRepository pointRepository;
 
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}
 
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ReaderJoined'"
    )
    public void wheneverReaderJoined_Givepoint(
        @Payload ReaderJoined readerJoined
    ) {
        ReaderJoined event = readerJoined;
        System.out.println(
            "\n\n##### listener Givepoint : " + readerJoined + "\n\n"
        );
 
        // Sample Logic //
        Point.givepoint(event);
        System.out.println(">>> Kafka 이벤트 publish 테스트: " + readerJoined.toJson());
 
    }
 
    // 해연님에게 받아오는 곳!
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PurchaseBookRequested'"
    )
    public void wheneverPurchaseBookRequested_ReadRemainingPoint(
        @Payload PurchaseBookRequested purchaseBookRequested
    ) {
        System.out.println(
            "\n\n##### listener ReadRemainingPoint : " +
            purchaseBookRequested +
            "\n\n"
        );
 
        Point point = Point.repository().findByReaderId(purchaseBookRequested.getReaderId())
        .orElseThrow(() -> new RuntimeException("포인트 계정 없음"));
 
 
        // Sample Logic //   // -> 이거 구현 수정 했어!!  -> 이제 여기서 보낸 거 결제에서 받아야함.
        Point.readRemainingPoint(purchaseBookRequested, point);
    }
 
    // 태현님
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PayRequested'"
    )
    public void wheneverPayRequested_ReadRemainingPoint(
        @Payload PayRequested payRequested
    ) {
        PayRequested event = payRequested;
        System.out.println(
            "\n\n##### listener ReadRemainingPoint : " + payRequested + "\n\n"
        );
 
        // Sample Logic //  readerId 하나 있어.
        Point.readRemainingPoint(event);
    }
 
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PaymentFinished'"
    )
    public void wheneverPaymentFinished_ChargePoint(
        @Payload PaymentFinished paymentFinished
    ) {
        System.out.println(
            "\n\n##### listener ChargePoint : " + paymentFinished + "\n\n"
        );
 
        // Sample Logic //
        Point.chargePoint(paymentFinished);
    }
 
    // @StreamListener(
    //     value = KafkaProcessor.INPUT,
    //     condition = "headers['type']=='RemainingPointChecked'"
    // )
    // public void wheneverRemainingPointChecked_UsePoint(
    //     @Payload RemainingPointChecked remainingPointChecked
    // ) {
    //     RemainingPointChecked event = remainingPointChecked;
    //     System.out.println(
    //         "\n\n##### listener UsePoint : " + remainingPointChecked + "\n\n"
    //     );
 
    //     // Sample Logic //
    //     Point.usePoint(event);
    // }
 
    // point 구매 거부
    // @StreamListener(
    //     value = KafkaProcessor.INPUT,
    //     condition = "headers['type']=='BuyRejected'"
    // )
    // public void wheneverBuyRejected_LeadAdditionalBuyAlert(
    //     @Payload BuyRejected buyRejected
    // ) {
    //     BuyRejected event = buyRejected;
    //     System.out.println(
    //         "\n\n##### listener LeadAdditionalBuyAlert : " +
    //         buyRejected +
    //         "\n\n"
    //     );
 
    //     // Sample Logic //
    //     Point.buyFail(event);
    // }
 
    // point 구매 성공
    // @StreamListener(
    //     value = KafkaProcessor.INPUT,
    //     condition = "headers['type']=='BuyApproved'"
    // )
    // public void wheneverBuyRejected_LeadAdditionalBuyAlert(
    //     @Payload BuyApproved buyApproved
    // ) {
    //     BuyApproved event = buyApproved;
    //     System.out.println(
    //         "\n\n##### listener LeadAdditionalBuyAlert : " +
    //         buyApproved +
    //         "\n\n"
    //     );
 
    //     // Sample Logic //
    //     Point.buyAccess(event);
    // }
 
 
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PaymentFailed'"
    )
    public void wheneverPaymentFailed_AlertPayFailed(
        @Payload PaymentFailed paymentFailed
    ) {
        PaymentFailed event = paymentFailed;
        System.out.println(
            "\n\n##### listener AlertPayFailed : " + paymentFailed + "\n\n"
        );
 
        // Sample Logic //
        Point.alertPayFailed(event);
    }
}
//>>> Clean Arch / Inbound Adaptor