package untitled.domain.Book;

import javax.persistence.Lob;

import lombok.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class BookRegistered extends AbstractEvent {

    private Long id;
    private String title;
    private Long authorId;
    private String authorName;
    private String contents;
    private String summary;
    private String imageUrl;
    private String category;
    private Integer price;
    private Long manuscriptId;

    public BookRegistered(Book aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.title = aggregate.getTitle();
        this.authorId = aggregate.getAuthorId();
        this.authorName = aggregate.getAuthorName();
        this.contents = aggregate.getContents();
        this.summary = aggregate.getSummary();
        this.imageUrl = aggregate.getImageUrl();
        this.category = aggregate.getCategory();
        this.price = aggregate.getPrice();
        this.manuscriptId = aggregate.getManuscriptId();
    }

    public BookRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
