package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class RegisterRequested extends AbstractEvent {

    private Long id;
    private String email;
    private String name;
    private String bio;
    private String majorWork;
    private File portfolio;
    private Boolean isApproval;

    public RegisterRequested(Author aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.email = aggregate.getEmail();
        this.name = aggregate.getName();
        this.bio = aggregate.getBio();
        this.majorWork = aggregate.getMajorWork();
        this.portfolio = aggregate.getPortfolio();
        this.isApproval = aggregate.getIsApproval();
    }

    public RegisterRequested() {
        super();
    }
}
//>>> DDD / Domain Event
