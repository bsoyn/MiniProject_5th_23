package untitled.domain.event;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.event.*;

import java.io.*;
import untitled.infra.AbstractEvent;
import untitled.domain.aggregate.Manuscript;

//<<< DDD / Domain Event
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PublicationRequested extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String title;
    private String contents;
    private String imageUrl;

    public PublicationRequested(Manuscript manuscript) {
        super(manuscript);
        this.id = manuscript.getId();
        this.authorId = manuscript.getAuthorId();
        this.title = manuscript.getTitle();
        this.contents = manuscript.getContents();
        this.imageUrl = manuscript.getImageUrl();
    }
}