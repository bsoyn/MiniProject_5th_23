package untitled.domain.Book;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class BookRegistered extends AbstractEvent {

    private Long id;
    private String title;
    private Long authorId;
    private String contents;
    private String summary;
    private String imageUrl;
    private String category;
    private Integer price;
    private Long manuscriptId;

    public BookRegistered(Book aggregate) {
        super(aggregate);
    }

    public BookRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
