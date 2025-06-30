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
import untitled.domain.NotPurchaseBookConfirmed;
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
    private String status; // "REQUESTED", "COMPLETED", "FAILED"

    @PostPersist
    public void onPostPersist() {
        // '도서구매 요청됨' 이벤트 발행
        PurchaseBookRequested requested = new PurchaseBookRequested(this);
        requested.publishAfterCommit(); 
    }

    public void markCompleted() {
        this.status = "COMPLETED";
    }

    public void markFailed() {
        this.status = "FAILED";
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
        this.readerId = purchasebookCommand.getReaderId();
        this.bookId = purchasebookCommand.getBookId();
        this.status = "REQUESTED";
    }
