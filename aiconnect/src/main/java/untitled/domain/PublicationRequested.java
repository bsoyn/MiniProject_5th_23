package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class PublicationRequested extends AbstractEvent {
    private Long id;
    private Long authorId;
    private String title;
    private String content;
    private String imageUrl;
}
