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

    // 추가된 승환님 부분 PointPaymentRequested
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PointPaymentRequested'"
    )
    public void wheneverPointPaymentRequested_SubscribeRequest(
        @Payload PointPaymentRequested pointPaymentRequested
    ) {
        PointPaymentRequested event = pointPaymentRequested;
        System.out.println(
            "\n\n##### listener SubscribeRequest : " + event + "\n\n"
        );

        // bookId가 null일 때만 구독으로 간주
        if (event.getBookId() == null) {
            Subscribe.subscribeRequest(event);
        }
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookAccessRequested'"
    )
    public void wheneverBookAccessRequested_SubscriptionValidCheck(
        @Payload BookAccessRequested bookAccessRequested
    ) {
        BookAccessRequested event = bookAccessRequested;
        System.out.println(
            "\n\n##### listener SubscriptionValidCheck : " +
            bookAccessRequested +
            "\n\n"
        );

        // Sample Logic //
        Subscribe.subscriptionValidCheck(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
