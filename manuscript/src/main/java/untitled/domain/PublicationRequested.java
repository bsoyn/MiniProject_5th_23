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
@NoArgsConstructor
@AllArgsConstructor
public class PublicationRequested extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String title;
    private String content;
    private String imageUrl;

    public PublicationRequested(Manuscript manuscript) {
        super(manuscript);
        this.id = manuscript.getId();
        this.authorId = manuscript.getAuthorId();
        this.title = manuscript.getTitle();
        this.content = manuscript.getContent();
        this.imageUrl = manuscript.getImageUrl();
    }
}