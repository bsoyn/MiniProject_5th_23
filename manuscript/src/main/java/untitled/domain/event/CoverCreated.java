package untitled.domain.event;

import java.util.*;
import lombok.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class CoverCreated extends AbstractEvent {
    private Long id;
    private Long manuscriptId; 
    private Long bookId;
    private String imageUrl;
}
