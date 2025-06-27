package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class ReaderJoined extends AbstractEvent {

    private Long id;
    private String email;
    private String password;
    private String name;
    private Boolean isSubscribe;
}
