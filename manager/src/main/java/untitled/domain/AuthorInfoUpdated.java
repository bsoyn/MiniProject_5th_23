package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;
import untitled.infra.AbstractEvent;

@Data
public class AuthorInfoUpdated extends AbstractEvent {

    private Long id;
    private String email;
    private String name;
    private String bio;
    private String majorWork;
    private File portfolio;
    private Boolean isApproval;
}
