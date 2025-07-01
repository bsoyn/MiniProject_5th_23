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
//<<< DDD / Aggregate Root
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long readerId;

    private LocalDate subscribeStartDate;

    private LocalDate subscribeEndDate;

    @PostPersist
    public void onPostPersist() {
        PayRequested payRequested = new PayRequested(this);
        payRequested.publishAfterCommit();

        // SubscriptionValidChecked subscriptionValidChecked = new SubscriptionValidChecked(
        //     this
        // );
        // subscriptionValidChecked.publishAfterCommit();

        // SubscriptionFinished subscriptionFinished = new SubscriptionFinished(
        //     this
        // );
        // subscriptionFinished.publishAfterCommit();
        
        // SubscriptionCompleted subscriptionCompleted = new SubscriptionCompleted(
        //     this
        // );
        // subscriptionCompleted.publishAfterCommit();
    }

    public static SubscribeRepository repository() {
        SubscribeRepository subscribeRepository = SubscriptionApplication.applicationContext.getBean(
            SubscribeRepository.class
        );
        return subscribeRepository;
    }

    //<<< Clean Arch / Port Method
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

        //implement business logic here:

        /** Example 1:  new item 
        Subscribe subscribe = new Subscribe();
        repository().save(subscribe);

        SubscriptionCompleted subscriptionCompleted = new SubscriptionCompleted(subscribe);
        subscriptionCompleted.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(buyApproved.get???()).ifPresent(subscribe->{
            
            subscribe // do something
            repository().save(subscribe);

            SubscriptionCompleted subscriptionCompleted = new SubscriptionCompleted(subscribe);
            subscriptionCompleted.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void subscribeFailAlert(BuyRejected buyRejected) {
        SubscriptionFailed event = new SubscriptionFailed();
        event.setReaderId(buyRejected.getReaderId());
        event.setReason("포인트 부족으로 결제 실패");  // 필요에 따라 수정 가능

        event.publishAfterCommit();

        System.out.println("구독 실패 알림 이벤트 전송 완료: " + event);
        
        
        //implement business logic here:

        /** Example 1:  new item 
        Subscribe subscribe = new Subscribe();
        repository().save(subscribe);

        */

        /** Example 2:  finding and process
        

        repository().findById(buyRejected.get???()).ifPresent(subscribe->{
            
            subscribe // do something
            repository().save(subscribe);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void subscriptionValidCheck(
        BookAccessRequested bookAccessRequested
    ) {
        Long readerId = bookAccessRequested.getReaderId();
        Long bookId = bookAccessRequested.getBookId();

        Optional<Subscribe> optional = repository().findByReaderId(readerId);

        if (optional.isPresent()) {
            Subscribe subscribe = optional.get();

            if (subscribe.getSubscribeEndDate().isAfter(LocalDate.now())) {
                SubscriptionValidChecked event = new SubscriptionValidChecked();
                event.setReaderId(readerId);
                event.setBookId(bookId);
                event.setIsSubscribe(true);
                event.publishAfterCommit();
            } else {
                SubscriptionFinished event = new SubscriptionFinished();
                event.setReaderId(readerId);
                event.setBookId(bookId);
                event.setIsSubscribe(false);
                event.publishAfterCommit();
            }
        } else {
            SubscriptionFinished event = new SubscriptionFinished();
            event.setReaderId(readerId);
            event.setBookId(bookId);
            event.setIsSubscribe(false);
            event.publishAfterCommit();
        }

        //implement business logic here:

        /** Example 1:  new item 
        Subscribe subscribe = new Subscribe();
        repository().save(subscribe);

        SubscriptionValidChecked subscriptionValidChecked = new SubscriptionValidChecked(subscribe);
        subscriptionValidChecked.publishAfterCommit();
        SubscriptionFinished subscriptionFinished = new SubscriptionFinished(subscribe);
        subscriptionFinished.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(boolAccessRequested.get???()).ifPresent(subscribe->{
            
            subscribe // do something
            repository().save(subscribe);

            SubscriptionValidChecked subscriptionValidChecked = new SubscriptionValidChecked(subscribe);
            subscriptionValidChecked.publishAfterCommit();
            SubscriptionFinished subscriptionFinished = new SubscriptionFinished(subscribe);
            subscriptionFinished.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
