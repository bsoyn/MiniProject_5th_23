package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import untitled.SubscriptionApplication;
import untitled.domain.PayRequested;
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

    private Date subscribeStartDate;

    private Date subscribeEndDate;

    @PostPersist
    public void onPostPersist() {
        PayRequested payRequested = new PayRequested(this);
        payRequested.publishAfterCommit();

        SubscriptionValidChecked subscriptionValidChecked = new SubscriptionValidChecked(
            this
        );
        subscriptionValidChecked.publishAfterCommit();

        SubscriptionFinished subscriptionFinished = new SubscriptionFinished(
            this
        );
        subscriptionFinished.publishAfterCommit();
    }

    public static SubscribeRepository repository() {
        SubscribeRepository subscribeRepository = SubscriptionApplication.applicationContext.getBean(
            SubscribeRepository.class
        );
        return subscribeRepository;
    }

    //<<< Clean Arch / Port Method
    public static void subscribeFinish(BuyApproved buyApproved) {
        //implement business logic here:

        /** Example 1:  new item 
        Subscribe subscribe = new Subscribe();
        repository().save(subscribe);

        */

        /** Example 2:  finding and process
        

        repository().findById(buyApproved.get???()).ifPresent(subscribe->{
            
            subscribe // do something
            repository().save(subscribe);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void subscribeFailAlert(BuyRejected buyRejected) {
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
        BoolAccessRequested boolAccessRequested
    ) {
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
