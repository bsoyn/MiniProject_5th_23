package untitled.infra;

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
    PaymentRepository paymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PointPaymentRequested'"
    )
    public void wheneverPointPaymentRequested_Pointpayment(
        @Payload PointPaymentRequested pointPaymentRequested
    ) {
        PointPaymentRequested event = pointPaymentRequested;
        System.out.println(
            "\n\n##### listener Pointpayment : " +
            pointPaymentRequested +
            "\n\n"
        );

        // Sample Logic //
        Payment.pointpayment(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PaymentFinished'"
    )
    public void handlePaymentFinished(@Payload PaymentFinished event) {
        System.out.println("✅ 결제 성공 수신: " + event);

        PointUsageRequested usage = new PointUsageRequested();
        usage.setReaderId(event.getReaderId());
        usage.setPoint(event.getPoint());
        usage.setPaymentId(event.getId());
        usage.setIsCompleted(true);  // ✅ 성공

        usage.publishAfterCommit();
    }


    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PaymentFailed'"
    )
    public void handlePaymentFailed(@Payload PaymentFailed event) {
        System.out.println("❌ 결제 실패 수신: " + event);

        PointUsageRequested usage = new PointUsageRequested();
        usage.setReaderId(event.getReaderId());
        usage.setPoint(event.getPoint());
        usage.setPaymentId(event.getId());
        usage.setIsCompleted(false);  // ❌ 실패

        usage.publishAfterCommit();
    }


}
//>>> Clean Arch / Inbound Adaptor
