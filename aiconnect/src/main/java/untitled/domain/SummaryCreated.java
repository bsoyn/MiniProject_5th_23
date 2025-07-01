package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class SummaryCreated extends AbstractEvent {

    private Long id;
    private Long manuscriptId; 
    private Long bookId;
    private String summary;
    private String category;
    private Integer price;
    private String keywords; 

    public SummaryCreated(BookSummary bookSummary) {
        super(bookSummary);
        this.setId(bookSummary.getId());
        this.setManuscriptId(bookSummary.getManuscriptId()); 
        this.setBookId(bookSummary.getBookId()); 
        this.setSummary(bookSummary.getSummary());
        this.setCategory(bookSummary.getCategory());
        this.setPrice(bookSummary.getPrice());   
        this.setKeywords(bookSummary.getKeywords());
    }

    public SummaryCreated() {
        super();
    }
}
//>>> DDD / Domain Event
