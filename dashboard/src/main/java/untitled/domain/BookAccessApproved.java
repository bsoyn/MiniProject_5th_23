package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import java.io.File;      
import lombok.Data;
import untitled.infra.AbstractEvent;

@Data
public class BookAccessApproved extends AbstractEvent {

    private Long id;
    private String title;
    private Long authorId;
    private File contents;
    private String summary;
    private String imageUrl;
    private String category;
    private Integer price;
    private Long views;
    private Boolean isPurchased;
    private Long readerId;
}
