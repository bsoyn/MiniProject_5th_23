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
public class AvailiableBookListViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private AvailiableBookListRepository availiableBookListRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookAccessApproved_then_CREATE_1(
        @Payload BookAccessApproved bookAccessApproved
    ) {
        try {
            if (!bookAccessApproved.validate()) return;

            // view 객체 생성
            AvailiableBookList availiableBookList = new AvailiableBookList();
            // view 객체에 이벤트의 Value 를 set 함
            availiableBookList.setBookid(bookAccessApproved.getId());
            availiableBookList.setReaderid(bookAccessApproved.getReaderId());
            availiableBookList.setIsPurchased(
                bookAccessApproved.getIsPurchased()
            );
            availiableBookList.setReadStart(false);
            // view 레파지 토리에 save
            availiableBookListRepository.save(availiableBookList);
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

            List<AvailiableBookList> availiableBookListList = availiableBookListRepository.findByBookid(
                purchasedCompleted.getBookId()
            );
            for (AvailiableBookList availiableBookList : availiableBookListList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                availiableBookList.setIsPurchased(true);
                // view 레파지 토리에 save
                availiableBookListRepository.save(availiableBookList);
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
            availiableBookListRepository.deleteByReaderid(
                subscriptionFinished.getReaderId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
