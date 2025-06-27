package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class BoolAccessRequested extends AbstractEvent {

    private Long id;
    private String title;
    private Long authorId;
    private Object contents;
    private String summary;
    private Object image;
    private Object category;
    private Float price;
    private Long readerId;
}
