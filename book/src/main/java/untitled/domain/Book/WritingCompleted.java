package untitled.domain.Book;

import lombok.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class WritingCompleted extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String authorName;
    private String title;
    private String contents;
    private String imageUrl;
    private String summary;
    private String category;
    private Integer price;
    private Long manuscriptId;
}