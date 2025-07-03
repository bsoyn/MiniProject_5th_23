package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.*;
import javax.persistence.*;
import lombok.Data;
import untitled.SubscriptionApplication;
import untitled.domain.PayRequested;
import untitled.domain.SubscriptionCompleted;
import untitled.domain.SubscriptionFinished;
import untitled.domain.SubscriptionValidChecked;
import untitled.domain.PointPaymentRequested;

@Entity
@Table(name = "Subscribe_table")
@Data
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long readerId;

    private LocalDate subscribeStartDate;

    private LocalDate subscribeEndDate;

    @PostPersist
    public void onPostPersist() {
        System.out.println(">>> Subscribe persisted: " + this);
        PayRequested payRequested = new PayRequested(this);
        payRequested.publishAfterCommit();
    }

    public static SubscribeRepository repository() {
        SubscribeRepository subscribeRepository = SubscriptionApplication.applicationContext.getBean(
            SubscribeRepository.class
        );
        return subscribeRepository;
    }

    public static void subscribeRequest(PointPaymentRequested event) {
        if (event.getBookId() != null) {
            // 도서 구매 요청은 무시
            return;
        }

        if (Boolean.TRUE.equals(event.getPurchase())) {
            subscribeCompleteAlert(event);
        } else {
            subscribeFailAlert(event);
        }
    }

    public static void subscribeCompleteAlert(PointPaymentRequested event) {
        Subscribe subscribe = new Subscribe();
        subscribe.setReaderId(event.getReaderId());
        subscribe.setSubscribeStartDate(LocalDate.now());
        subscribe.setSubscribeEndDate(LocalDate.now().plusMonths(1));

        repository().save(subscribe);

        SubscriptionCompleted completedEvent = new SubscriptionCompleted(subscribe);
        completedEvent.publishAfterCommit();

        System.out.println("구독 완료 이벤트 발행: " + completedEvent);
    }

    public static void subscribeFailAlert(PointPaymentRequested event) {
        SubscriptionFailed failedEvent = new SubscriptionFailed();
        failedEvent.setReaderId(event.getReaderId());
        failedEvent.setReason("포인트 부족으로 구독 실패");

        failedEvent.publishAfterCommit();

        System.out.println("구독 실패 이벤트 발행: " + failedEvent);
    }
    
    public static void subscriptionValidCheck(
        BookAccessRequested bookAccessRequested
    ) {
        Long readerId = bookAccessRequested.getReaderId();
        Long bookId = bookAccessRequested.getBookId();
        Long Id = bookAccessRequested.getId();
        
        Optional<Subscribe> optional = repository().findByReaderId(readerId);

        if (optional.isPresent()) {
            Subscribe subscribe = optional.get();

            if (subscribe.getSubscribeEndDate() != null &&
                subscribe.getSubscribeEndDate().isAfter(LocalDate.now())) {
                SubscriptionValidChecked event = new SubscriptionValidChecked(Id ,readerId, bookId, true);
                event.publishAfterCommit();
            } else {
                SubscriptionFinished event = new SubscriptionFinished(Id, readerId, bookId, false);
                event.publishAfterCommit();
            }
        } else {
            SubscriptionFinished event = new SubscriptionFinished(
                bookAccessRequested.getId(),
                readerId,
                bookId,
                false
            );
            event.publishAfterCommit();
        }
    }
}
