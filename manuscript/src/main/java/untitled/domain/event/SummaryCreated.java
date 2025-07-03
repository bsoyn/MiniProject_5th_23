package untitled.domain.event;

import java.util.*;
import lombok.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class SummaryCreated extends AbstractEvent {
    private Long id;
    private Long manuscriptId; 
    private Long bookId;
    private String summary;
    private String category;
    private Integer price;
}
