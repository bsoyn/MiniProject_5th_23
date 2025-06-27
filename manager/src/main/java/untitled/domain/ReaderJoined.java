package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;
import untitled.infra.AbstractEvent;

@Data
public class ReaderJoined extends AbstractEvent {

    private Long id;
    private String email;
    private String password;
    private String name;
    private Boolean isSubscribe;
}
