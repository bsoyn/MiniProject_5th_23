package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import untitled.AiconnectApplication;
import untitled.domain.SummaryCreated;

@Entity
@Table(name = "BookSummary_table")
@Data
//<<< DDD / Aggregate Root
public class BookSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long bookId;

    private String summary;

    private String category;

    private Integer price;

    public static BookSummaryRepository repository() {
        BookSummaryRepository bookSummaryRepository = AiconnectApplication.applicationContext.getBean(
            BookSummaryRepository.class
        );
        return bookSummaryRepository;
    }

    //<<< Clean Arch / Port Method
    public static void summaryBook(PublicationRequested publicationRequested) {
        //implement business logic here:

        /** Example 1:  new item 
        BookSummary bookSummary = new BookSummary();
        repository().save(bookSummary);

        SummaryCreated summaryCreated = new SummaryCreated(bookSummary);
        summaryCreated.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(publicationRequested.get???()).ifPresent(bookSummary->{
            
            bookSummary // do something
            repository().save(bookSummary);

            SummaryCreated summaryCreated = new SummaryCreated(bookSummary);
            summaryCreated.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
