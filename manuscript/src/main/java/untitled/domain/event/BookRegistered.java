package untitled.domain.event;

import lombok.*;
import untitled.infra.AbstractEvent;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRegistered extends AbstractEvent {
    private Long id;
    private Long manuscriptId; 
    private String title;
    private Long authorId;
    private String contents;
    private String summary;
    private String imageUrl;
    private String category;
    private Integer price;
}
