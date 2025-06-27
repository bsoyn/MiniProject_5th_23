package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class AuthorInfoUpdated extends AbstractEvent {

    private Long id;
    private String email;
    private String name;
    private String bio;
    private String majorWork;
    private File portfolio;
    private Boolean isApproval;

    public AuthorInfoUpdated(Author aggregate) {
        super(aggregate);
    }

    public AuthorInfoUpdated() {
        super();
    }
}
//>>> DDD / Domain Event
