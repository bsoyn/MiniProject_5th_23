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
public class AvailableBookViewHandler {

    //<<< DDD / CQRS

    private final BookClient bookClient;
    private final ReaderClient readerClient;
    private final AvailableBookViewRepository availableBookViewRepository;

    public AvailableBookViewHandler(BookClient bookClient, ReaderClient readerClient, AvailableBookViewRepository availableBookViewRepository){
        this.bookClient = bookClient;
        this.readerClient = readerClient;
        this.availableBookViewRepository = availableBookViewRepository;
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookAccessApproved_then_CREATE_1(
        @Payload BookAccessApproved bookAccessApproved
    ) {
        try {
            if (!bookAccessApproved.validate()) return;

            // view 객체 생성
            AvailableBookView view = new AvailableBookView();
            // view 객체에 이벤트의 Value 를 set 함
            view.setBookId(bookAccessApproved.getBookId());
            view.setReaderId(bookAccessApproved.getReaderId());
            view.setIsPurchased(
                bookAccessApproved.getIsPurchased()
            );
            view.setReadStart(false);


            // view 레파지 토리에 save
            availableBookViewRepository.save(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenPurchasedCompleted_then_UPDATE_1(
        @Payload PurchasedCompleted purchasedCompleted
    ) {
        try {
            if (!purchasedCompleted.validate()) return;
            // view 객체 조회

            List<AvailableBookView> availiableBookListList = availableBookViewRepository.findByBookid(
                purchasedCompleted.getBookId()
            );
            for (AvailableBookView availiableBookList : availiableBookListList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                availiableBookList.setIsPurchased(true);
                // view 레파지 토리에 save
                availableBookViewRepository.save(availiableBookList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenSubscriptionFinished_then_DELETE_1(
        @Payload SubscriptionFinished subscriptionFinished
    ) {
        try {
            if (!subscriptionFinished.validate()) return;
            // view 레파지 토리에 삭제 쿼리
            availableBookViewRepository.deleteByReaderid(
                subscriptionFinished.getReaderId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
