package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class CoverCreated extends AbstractEvent {

    private Long id;
    private Long bookId;
    private String imageUrl;
}
