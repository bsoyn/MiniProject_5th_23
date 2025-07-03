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

    public static SubscribeRepository repository() {
        SubscribeRepository subscribeRepository = SubscriptionApplication.applicationContext.getBean(
            SubscribeRepository.class
        );
        return subscribeRepository;
    }

    // Point로 부터 구독 성공 실패 여부 확인
    public static void subscribeRequest(PointPaymentRequested event) {
        if (event.getBookId() != null) {
            // 도서 구매 요청은 무시
            return;
        }

        // Purchase로 성공 실패 판단
        if (Boolean.TRUE.equals(event.getPurchase())) {
            subscribeCompleteAlert(event);
        } else {
            subscribeFailAlert(event);
        }
    }

    // 구독 성공 시
    public static void subscribeCompleteAlert(PointPaymentRequested event) {
        Optional<Subscribe> existingSubscribe = repository().findByReaderId(event.getReaderId());
        Subscribe subscribe;

        if (existingSubscribe.isPresent()) {
            subscribe = existingSubscribe.get();
            System.out.println("기존 구독 객체 업데이트: " + subscribe.getId());
        } else {
            subscribe = new Subscribe();
            subscribe.setReaderId(event.getReaderId());
            System.out.println("새 구독 객체 생성: " + subscribe.getReaderId());
        }

        // 구독 성공 시 시작일과 종료일 업데이트
        subscribe.setSubscribeStartDate(LocalDate.now());
        subscribe.setSubscribeEndDate(LocalDate.now().plusMonths(1));

        repository().save(subscribe);

        SubscriptionCompleted completedEvent = new SubscriptionCompleted(subscribe);
        completedEvent.publishAfterCommit();

        System.out.println("구독 완료 이벤트 발행: " + completedEvent);
    }

    // 구독 실패 시
    public static void subscribeFailAlert(PointPaymentRequested event) {
        Optional<Subscribe> existingSubscribe = repository().findByReaderId(event.getReaderId());
        Subscribe subscribe;

        if (existingSubscribe.isPresent()) {
            subscribe = existingSubscribe.get();
            System.out.println("기존 구독 객체 업데이트 (실패): " + subscribe.getId());
        } else {
            subscribe = new Subscribe();
            subscribe.setReaderId(event.getReaderId());
            System.out.println("새 구독 객체 생성 (실패): " + subscribe.getReaderId());
        }

        // 구독 실패 시 startdate와 enddate를 null로 설정
        subscribe.setSubscribeStartDate(null);
        subscribe.setSubscribeEndDate(null);

        repository().save(subscribe);

        SubscriptionFailed failedEvent = new SubscriptionFailed();
        failedEvent.setReaderId(event.getReaderId());
        failedEvent.setReason("포인트 부족으로 구독 실패");

        failedEvent.publishAfterCommit();

        System.out.println("구독 실패 이벤트 발행 및 실패 기록 생성: " + failedEvent);
    }
    
    // 도서 열람 시 구독 여부 확인
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
