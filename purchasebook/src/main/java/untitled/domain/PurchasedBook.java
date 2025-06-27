package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import untitled.PurchasebookApplication;
import untitled.domain.NotParchaseBookConfirmed;
import untitled.domain.PurchaseBookConfirmed;
import untitled.domain.PurchasedCompleted;

@Entity
@Table(name = "PurchasedBook_table")
@Data
//<<< DDD / Aggregate Root
public class PurchasedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long readerId;

    private Long bookId;

    @PostPersist
    public void onPostPersist() {
        PurchaseBookConfirmed purchaseBookConfirmed = new PurchaseBookConfirmed(
            this
        );
        purchaseBookConfirmed.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        untitled.external.PurchasebookCommand purchasebookCommand = new untitled.external.PurchasebookCommand();
        // mappings goes here
        PurchasebookApplication.applicationContext
            .getBean(untitled.external.PurchasedBookService.class)
            .purchasebook(/* get???(), */purchasebookCommand);

        NotParchaseBookConfirmed notParchaseBookConfirmed = new NotParchaseBookConfirmed(
            this
        );
        notParchaseBookConfirmed.publishAfterCommit();

        PurchasedCompleted purchasedCompleted = new PurchasedCompleted(this);
        purchasedCompleted.publishAfterCommit();
    }

    public static PurchasedBookRepository repository() {
        PurchasedBookRepository purchasedBookRepository = PurchasebookApplication.applicationContext.getBean(
            PurchasedBookRepository.class
        );
        return purchasedBookRepository;
    }

    //<<< Clean Arch / Port Method
    public void purchasebook(PurchasebookCommand purchasebookCommand) {
        //implement business logic here:

        PurchaseBookRequested purchaseBookRequested = new PurchaseBookRequested(
            this
        );
        purchaseBookRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void purchaseFinishAlert(BuyApproved buyApproved) {
        //implement business logic here:

        /** Example 1:  new item 
        PurchasedBook purchasedBook = new PurchasedBook();
        repository().save(purchasedBook);

        PurchasedCompleted purchasedCompleted = new PurchasedCompleted(purchasedBook);
        purchasedCompleted.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(buyApproved.get???()).ifPresent(purchasedBook->{
            
            purchasedBook // do something
            repository().save(purchasedBook);

            PurchasedCompleted purchasedCompleted = new PurchasedCompleted(purchasedBook);
            purchasedCompleted.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void purchaseFailAlert(BuyRejected buyRejected) {
        //implement business logic here:

        /** Example 1:  new item 
        PurchasedBook purchasedBook = new PurchasedBook();
        repository().save(purchasedBook);

        */

        /** Example 2:  finding and process
        

        repository().findById(buyRejected.get???()).ifPresent(purchasedBook->{
            
            purchasedBook // do something
            repository().save(purchasedBook);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
