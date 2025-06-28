package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

import java.io.*; 

//<<< DDD / Domain Event
@Data
@ToString
public class PublicationRequested extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String title;
    private File content;
    private String imageUrl;

    public PublicationRequested(Manuscript aggregate) {
        super(aggregate);
    }

    public PublicationRequested() {
        super();
    }
}
//>>> DDD / Domain Event
