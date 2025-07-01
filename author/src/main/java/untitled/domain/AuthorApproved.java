package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class AuthorApproved extends AbstractEvent {

    private Long id;
    private String email;
    private Boolean isApproval;

    public AuthorApproved() {
        super();
    }
}
