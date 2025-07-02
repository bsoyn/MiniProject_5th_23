package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class AuthorApproved extends AbstractEvent {

        private Long id;
    private String email;
    private Boolean isApproval;

    public AuthorApproved(ManageAuthor aggregate) {
        super(aggregate);
                this.id = aggregate.getId();
        this.email = aggregate.getEmail();
        this.isApproval = aggregate.getIsApproval();
    }

    public AuthorApproved() {
        super();
    }
}
//>>> DDD / Domain Event
