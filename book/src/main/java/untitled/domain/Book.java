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
import untitled.domain.BooKAccessRequested;

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

    private String contents;

    private String summary;

    private String imageUrl;

    private String category;

    private Integer price;

    private Long views;

    @PostPersist
    public void onPostPersist() {
        BooKAccessRequested bookAccessRequested = new BooKAccessRequested(this);
        bookAccessRequested.publishAfterCommit();

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
        BooKAccessRequested bookAccessRequested = new BooKAccessRequested();
        bookAccessRequested.setBookId(requestbookAuthorityCommand.getBookId());
        bookAccessRequested.setReaderId(requestbookAuthorityCommand.getReaderId());
        bookAccessRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void bookAccessApproveAlert(
        SubscriptionValidChecked subscriptionValidChecked
    ) {
        repository().findById(subscriptionValidChecked.getBookId()).ifPresent(book->{
            // do something
            BookAccessApproved bookAccessApproved = new BookAccessApproved(book);
            bookAccessApproved.setReaderId(subscriptionValidChecked.getReaderId());

            bookAccessApproved.publishAfterCommit();
         });

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void bookAccessApproveAlert(
        PurchaseBookConfirmed purchaseBookConfirmed
    ) {
        repository().findById(purchaseBookConfirmed.getBookId()).ifPresent(book->{

            BookAccessApproved bookAccessApproved = new BookAccessApproved(book);
            
            bookAccessApproved.setIsPurchased(true);
            bookAccessApproved.setReaderId(purchaseBookConfirmed.getReaderId());

            bookAccessApproved.publishAfterCommit();
         });

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
    //<<< Clean Arch / Port Method
    public static void requestBookRegistrationAlert(
        WritingCompleted writingCompleted
    ) {
        //implement business logic here:

        /** Example 1:  new item **/
        Book book = new Book();
        book.authorId = writingCompleted.getAuthorId();
        book.category = writingCompleted.getCategory();
        book.contents = writingCompleted.getContents();
        book.imageUrl = writingCompleted.getImageUrl();
        book.price = writingCompleted.getPrice();
        book.summary = writingCompleted.getSummary();
        book.title = writingCompleted.getTitle();

        repository().save(book);

        BookRegistered bookRegistered = new BookRegistered(book);
        bookRegistered.publishAfterCommit();
        

        /** Example 2:  finding and process
        

        repository().findById(writingCompleted.get???()).ifPresent(book->{
            
            book // do something
            repository().save(book);

            BookRegistered bookRegistered = new BookRegistered(book);
            bookRegistered.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method
}
//>>> DDD / Aggregate Root
