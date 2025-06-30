package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;
import java.io.File;

@Data
@ToString
public class WritingCompleted extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String title;
    private String contents;
    private String imageUrl;
    private String summary;
    private String category;
    private Integer price;
    private Long manuscriptId;
    
}