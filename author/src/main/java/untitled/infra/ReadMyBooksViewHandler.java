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
public class ReadMyBooksViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private ReadMyBooksRepository readMyBooksRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenWritingCompleted_then_CREATE_1(
        @Payload WritingCompleted writingCompleted
    ) {
        try {
            if (!writingCompleted.validate()) return;

            // view 객체 생성
            ReadMyBooks readMyBooks = new ReadMyBooks();
            // view 객체에 이벤트의 Value 를 set 함
            readMyBooks.setId(writingCompleted.getId());
            readMyBooks.setAuthorId(writingCompleted.getAuthorId());
            readMyBooks.setTitle(writingCompleted.getTitle());
            readMyBooks.setContent(writingCompleted.getContent());
            readMyBooks.setSummary(writingCompleted.getSummary());
            readMyBooks.setImage(String.valueOf(writingCompleted.getImage()));
            readMyBooks.setCategory(writingCompleted.getCategory());
            readMyBooks.setPrice(writingCompleted.getPrice());
            // view 레파지 토리에 save
            readMyBooksRepository.save(readMyBooks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
