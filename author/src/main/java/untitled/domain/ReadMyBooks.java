package untitled.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "ReadMyBooks_table")
@Data
public class ReadMyBooks {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long authorId;
    private String title;
    private String content;
    private String summary;
    private String image;
    private String category;
    private Integer price;
}
