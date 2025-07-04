package untitled.infra;

import java.util.Optional;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import untitled.config.kafka.KafkaProcessor;
import untitled.domain.*;

@Service
public class AvailableBookViewHandler {

    //<<< DDD / CQRS

    private final BookClient bookClient;
    private final AvailableBookViewRepository availableBookViewRepository;

    public AvailableBookViewHandler(BookClient bookClient, AvailableBookViewRepository availableBookViewRepository){
        this.bookClient = bookClient;
        this.availableBookViewRepository = availableBookViewRepository;
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookAccessApproved_then_CREATE_1(
        @Payload BookAccessApproved bookAccessApproved
    ) {
        try {
            if (!bookAccessApproved.validate()) return;

            Optional<AvailableBookView> viewOpt = availableBookViewRepository
                .findByReaderIdAndBookId(bookAccessApproved.getReaderId(), bookAccessApproved.getBookId());

            
            if (viewOpt.isPresent()) {
                AvailableBookView view = viewOpt.get();
                view.setIsPurchased(bookAccessApproved.getIsPurchased());

                // view 레파지 토리에 save
                availableBookViewRepository.save(view);
            }else {
                // view 객체 생성
                AvailableBookView view = new AvailableBookView();
                // view 객체에 이벤트의 Value 를 set 함
                view.setReaderId(bookAccessApproved.getReaderId());
                view.setIsPurchased(
                    bookAccessApproved.getIsPurchased()
                );
                view.setReadStart(true);

                BookDto book = bookClient.getBook(bookAccessApproved.getId());
                view.setBookId(book.getBookId());
                view.setTitle(book.getTitle());
                view.setSummary(book.getSummary());
                view.setImageUrl(book.getImageUrl());
                view.setAuthorName(book.getAuthorName());
                view.setCategory(book.getCategory());
                view.setContents(book.getContents());

                availableBookViewRepository.save(view);
            }
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

            Optional<AvailableBookView> viewOpt = availableBookViewRepository
                .findByReaderIdAndBookId(purchasedCompleted.getReaderId(), purchasedCompleted.getBookId());
            
            if(viewOpt.isPresent()){
                AvailableBookView view = viewOpt.get();
                view.setIsPurchased(true);
                // view 레파지 토리에 save
                availableBookViewRepository.save(view);
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

            long deleted = availableBookViewRepository
                .deleteByReaderIdAndIsPurchasedFalse(subscriptionFinished.getReaderId());
                System.out.println("삭제된 미구매 리드모델 건 수 : " + deleted);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    //>>> DDD / CQRS
}
