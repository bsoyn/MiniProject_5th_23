package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import untitled.PointApplication;
import untitled.domain.BuyApproved;
import untitled.domain.BuyRejected;
import untitled.domain.PointPaymentRequested;
import untitled.domain.RemainingPointChecked;

@Entity
@Table(name = "Point_table")
@Data
//<<< DDD / Aggregate Root
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long readerId;

    private Integer point;

    @PreUpdate
    public void onPreUpdate() {
        PointPaymentRequested pointPaymentRequested = new PointPaymentRequested(
            this
        );
        pointPaymentRequested.publishAfterCommit();
    }

    public static PointRepository repository() {
        PointRepository pointRepository = PointApplication.applicationContext.getBean(
            PointRepository.class
        );
        return pointRepository;
    }

    //<<< Clean Arch / Port Method
    public static void givepoint(ReaderJoined readerJoined) {
        //implement business logic here:

        //** Example 1:  new item 
        Point point = new Point();
        point.setReaderId(readerJoined.getId())
        point.setPoint(5000);
        repository().save(point);

        //*/

        /** Example 2:  finding and process
        

        repository().findById(readerJoined.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void readRemainingPoint(
        PurchaseBookRequested purchaseBookRequested
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        RemainingPointChecked remainingPointChecked = new RemainingPointChecked(point);
        remainingPointChecked.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(purchaseBookRequested.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);

            RemainingPointChecked remainingPointChecked = new RemainingPointChecked(point);
            remainingPointChecked.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void readRemainingPoint(PayRequested payRequested) {
        //implement business logic here:

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        RemainingPointChecked remainingPointChecked = new RemainingPointChecked(point);
        remainingPointChecked.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(payRequested.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);

            RemainingPointChecked remainingPointChecked = new RemainingPointChecked(point);
            remainingPointChecked.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void chargePoint(PaymentFinished paymentFinished) {
        //implement business logic here:

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        */

        /** Example 2:  finding and process
        
        // if paymentFinished.externalPaymentModuleId exists, use it
        
        // ObjectMapper mapper = new ObjectMapper();
        // Map<, Object> paymentMap = mapper.convertValue(paymentFinished.getExternalPaymentModuleId(), Map.class);

        repository().findById(paymentFinished.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void usePoint(RemainingPointChecked remainingPointChecked) {
        //implement business logic here:

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        BuyApproved buyApproved = new BuyApproved(point);
        buyApproved.publishAfterCommit();
        BuyRejected buyRejected = new BuyRejected(point);
        buyRejected.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(remainingPointChecked.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);

            BuyApproved buyApproved = new BuyApproved(point);
            buyApproved.publishAfterCommit();
            BuyRejected buyRejected = new BuyRejected(point);
            buyRejected.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void leadAdditionalBuyAlert(BuyRejected buyRejected) {
        //implement business logic here:

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        */

        /** Example 2:  finding and process
        

        repository().findById(buyRejected.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void alertPayFailed(PaymentFailed paymentFailed) {
        //implement business logic here:

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        */

        /** Example 2:  finding and process
        
        // if paymentFailed.externalPaymentModuleId exists, use it
        
        // ObjectMapper mapper = new ObjectMapper();
        // Map<, Object> paymentMap = mapper.convertValue(paymentFailed.getExternalPaymentModuleId(), Map.class);

        repository().findById(paymentFailed.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
