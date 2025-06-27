package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class AuthorDenied extends AbstractEvent {

    private Long id;
    private Long authorId;
    private Boolean isApproval;

    public AuthorDenied(ManageAuthor aggregate) {
        super(aggregate);
    }

    public AuthorDenied() {
        super();
    }
}
//>>> DDD / Domain Event
