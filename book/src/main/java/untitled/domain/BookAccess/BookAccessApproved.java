package untitled.domain.BookAccess;

import lombok.*;
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

    public BookAccessApproved(BookAccess aggregate) {
        super(aggregate);
    }

    public BookAccessApproved() {
        super();
    }
}
//>>> DDD / Domain Event
