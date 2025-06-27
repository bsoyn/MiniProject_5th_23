package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class AuthorDenied extends AbstractEvent {

    private Long id;
    private Long authorId;
    private Boolean isApproval;
}
