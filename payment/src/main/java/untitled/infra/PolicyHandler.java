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
}
//>>> Clean Arch / Inbound Adaptor
