package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import untitled.AiconnectApplication;
import untitled.domain.CoverCreated;

@Entity
@Table(name = "BookCover_table")
@Data
//<<< DDD / Aggregate Root
public class BookCover {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long bookId;

    private String imageUrl;

    @PostPersist
    public void onPostPersist() {
        CoverCreated coverCreated = new CoverCreated(this);
        coverCreated.publishAfterCommit();
    }

    public static BookCoverRepository repository() {
        BookCoverRepository bookCoverRepository = AiconnectApplication.applicationContext.getBean(
            BookCoverRepository.class
        );
        return bookCoverRepository;
    }
}
//>>> DDD / Aggregate Root
