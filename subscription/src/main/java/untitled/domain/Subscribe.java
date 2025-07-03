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

    public static void subscribeFinish(BuyApproved buyApproved) {
        Subscribe subscribe = new Subscribe();
        subscribe.setReaderId(buyApproved.getReaderId());
        subscribe.setSubscribeStartDate(LocalDate.now());
        subscribe.setSubscribeEndDate(LocalDate.now().plusMonths(1));

        repository().save(subscribe);

        SubscriptionCompleted event = new SubscriptionCompleted();
        event.setReaderId(subscribe.getReaderId());
        event.setSubscribeStartDate(subscribe.getSubscribeStartDate());
        event.setSubscribeEndDate(subscribe.getSubscribeEndDate());
        event.publishAfterCommit();
    }

    public static void subscribeFailAlert(BuyRejected buyRejected) {
        SubscriptionFailed event = new SubscriptionFailed();
        event.setReaderId(buyRejected.getReaderId());
        event.setReason("포인트 부족으로 결제 실패");  // 필요에 따라 수정 가능

        event.publishAfterCommit();

        System.out.println("구독 실패 알림 이벤트 전송 완료: " + event);

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
