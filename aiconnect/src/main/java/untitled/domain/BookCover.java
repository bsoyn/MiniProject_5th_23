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

import java.io.IOException;

import org.springframework.beans.BeanUtils;
import okhttp3.*; // OkHttpClient, Request, RequestBody, MediaType, Response
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.concurrent.TimeUnit;


@Entity
@Table(name = "BookCover_table")
@Data
//<<< DDD / Aggregate Root
public class BookCover {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long manuscriptId; 

    private Long bookId;

    @Lob
    @Column(columnDefinition = "TEXT")
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
