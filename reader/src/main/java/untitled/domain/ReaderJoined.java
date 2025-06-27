package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class ReaderJoined extends AbstractEvent {

    private Long id;
    private String email;
    private String password;
    private String name;
    private Boolean isSubscribe;

    public ReaderJoined(ManagerReader aggregate) {
        super(aggregate);
    }

    public ReaderJoined() {
        super();
    }
}
//>>> DDD / Domain Event
