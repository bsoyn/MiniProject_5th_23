package untitled.infra;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import untitled.config.kafka.KafkaProcessor;
import untitled.domain.*;

@Service
public class ReadReaderInfoViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private ReadReaderInfoRepository readReaderInfoRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenReaderJoined_then_CREATE_1(
        @Payload ReaderJoined readerJoined
    ) {
        try {
            if (!readerJoined.validate()) return;

            // view 객체 생성
            ReadReaderInfo readReaderInfo = new ReadReaderInfo();
            // view 객체에 이벤트의 Value 를 set 함
            readReaderInfo.setReaderId(readerJoined.getId());
            readReaderInfo.setEmail(readerJoined.getEmail());
            readReaderInfo.setName(readerJoined.getName());
            readReaderInfo.setIsSubscribe(readerJoined.getIsSubscribe());
            // view 레파지 토리에 save
            readReaderInfoRepository.save(readReaderInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
