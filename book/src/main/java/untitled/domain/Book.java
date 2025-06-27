package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import untitled.BookApplication;
import untitled.domain.BookAccessApproved;
import untitled.domain.BookAccessRejected;
import untitled.domain.BookRegistered;
import untitled.domain.BoolAccessRequested;

@Entity
@Table(name = "Book_table")
@Data
//<<< DDD / Aggregate Root
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private Long authorId;

    @Embedded
    private File contents;

    private String summary;

    private String imageUrl;

    private String category;

    private Integer price;

    private Long views;

    @PostPersist
    public void onPostPersist() {
        BoolAccessRequested boolAccessRequested = new BoolAccessRequested(this);
        boolAccessRequested.publishAfterCommit();

        BookRegistered bookRegistered = new BookRegistered(this);
        bookRegistered.publishAfterCommit();

        BookAccessApproved bookAccessApproved = new BookAccessApproved(this);
        bookAccessApproved.publishAfterCommit();

        BookAccessRejected bookAccessRejected = new BookAccessRejected(this);
        bookAccessRejected.publishAfterCommit();
    }

    public static BookRepository repository() {
        BookRepository bookRepository = BookApplication.applicationContext.getBean(
            BookRepository.class
        );
        return bookRepository;
    }

    //<<< Clean Arch / Port Method
    public void requestbookAuthority(
        RequestbookAuthorityCommand requestbookAuthorityCommand
    ) {
        //implement business logic here:

    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void bookAccessApproveAlert(
        SubscriptionValidChecked subscriptionValidChecked
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Book book = new Book();
        repository().save(book);

        BookAccessApproved bookAccessApproved = new BookAccessApproved(book);
        bookAccessApproved.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(subscriptionValidChecked.get???()).ifPresent(book->{
            
            book // do something
            repository().save(book);

            BookAccessApproved bookAccessApproved = new BookAccessApproved(book);
            bookAccessApproved.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void bookAccessApproveAlert(
        PurchaseBookConfirmed purchaseBookConfirmed
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Book book = new Book();
        repository().save(book);

        BookAccessApproved bookAccessApproved = new BookAccessApproved(book);
        bookAccessApproved.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(purchaseBookConfirmed.get???()).ifPresent(book->{
            
            book // do something
            repository().save(book);

            BookAccessApproved bookAccessApproved = new BookAccessApproved(book);
            bookAccessApproved.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void bookAccessDeniedAlert(
        SubscriptionFinished subscriptionFinished
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Book book = new Book();
        repository().save(book);

        BookAccessRejected bookAccessRejected = new BookAccessRejected(book);
        bookAccessRejected.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(subscriptionFinished.get???()).ifPresent(book->{
            
            book // do something
            repository().save(book);

            BookAccessRejected bookAccessRejected = new BookAccessRejected(book);
            bookAccessRejected.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void bookAccessDeniedAlert(
        NotParchaseBookConfirmed notParchaseBookConfirmed
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Book book = new Book();
        repository().save(book);

        BookAccessRejected bookAccessRejected = new BookAccessRejected(book);
        bookAccessRejected.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(notParchaseBookConfirmed.get???()).ifPresent(book->{
            
            book // do something
            repository().save(book);

            BookAccessRejected bookAccessRejected = new BookAccessRejected(book);
            bookAccessRejected.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
