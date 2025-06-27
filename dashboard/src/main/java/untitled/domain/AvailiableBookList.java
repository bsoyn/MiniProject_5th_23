package untitled.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "AvailiableBookList_table")
@Data
public class AvailiableBookList {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long bookid;
    private Long readerid;
    private Boolean isPurchased;
    private Boolean readStart;
}
