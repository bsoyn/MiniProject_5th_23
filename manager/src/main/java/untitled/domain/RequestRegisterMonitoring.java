package untitled.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "RequestRegisterMonitoring_table")
@Data
public class RequestRegisterMonitoring {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long authorId;
    private Boolean isApproval;
    private String name;
    private String bio;
    private String majorWork;
    private String portfolio;
    private String email;
}
