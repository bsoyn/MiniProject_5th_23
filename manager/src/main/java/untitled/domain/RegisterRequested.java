package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

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
}
