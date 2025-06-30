package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class BookRegistered extends AbstractEvent {
    private Long id;
    private Long manuscriptId; 
    private String title;
    private Long authorId;
    private Object contents;
    private String summary;
    private String imageUrl;
    private String category;
    private Integer price;
}
