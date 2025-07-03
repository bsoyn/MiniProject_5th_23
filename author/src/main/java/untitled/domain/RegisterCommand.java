package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;
import untitled.domain.File;

@Data
public class RegisterCommand {

    private String email;
    private String password;
    private String name;
    private String bio;
    private String majorWork;
    private File portfolio;
    private Boolean isApproval;
    private Long id;
}
