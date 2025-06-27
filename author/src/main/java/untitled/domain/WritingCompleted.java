package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;
import untitled.infra.AbstractEvent;

@Data
public class WritingCompleted extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String title;
    private String content;
    private File image;
    private String summary;
    private String category;
    private Integer price;
}
