package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class BookAccessApproved extends AbstractEvent {

    private Long id;
    private String title;
    private Long authorId;
    private String contents;
    private String summary;
    private String imageUrl;
    private String category;
    private Integer price;
    private Long views;
    private Boolean isPurchased;
    private Long readerId;

    public BookAccessApproved(Book aggregate) {
        super(aggregate);
        this.title = aggregate.getTitle();
        this.authorId = aggregate.getAuthorId();
        this.contents = aggregate.getContents();
        this.summary = aggregate.getSummary();
        this.imageUrl = aggregate.getImageUrl();
        this.category = aggregate.getCategory();
        this.price = aggregate.getPrice();
        this.views = aggregate.getViews();
    }

    public BookAccessApproved() {
        super();
    }
}
//>>> DDD / Domain Event
