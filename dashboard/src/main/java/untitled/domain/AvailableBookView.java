package untitled.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "AvailableBookView_table")
@Data
public class AvailableBookView {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long bookId;
    private String title;
    private String authorName;
    private String summary;
    private String imageUrl;
    private String category;
    private String contents;

    private Long readerId;
    private String readerName;

    private Boolean isPurchased;
    private Boolean readStart;
}
