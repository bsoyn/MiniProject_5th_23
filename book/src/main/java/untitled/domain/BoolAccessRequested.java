package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import java.io.File;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class BoolAccessRequested extends AbstractEvent {

    private Long id;
    private String title;
    private Long authorId;
    private File contents;
    private String summary;
    private File image;
    private String category;
    private Float price;
    private Long readerId;

    public BoolAccessRequested(Book aggregate) {
        super(aggregate);
    }

    public BoolAccessRequested() {
        super();
    }
}
//>>> DDD / Domain Event
