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
import untitled.domain.SummaryCreated;

import untitled.domain.SummaryCreated;
import javax.persistence.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Entity
@Table(name = "BookSummary_table")
@Data
//<<< DDD / Aggregate Root
public class BookSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long bookId;

    private Long manuscriptId; 

    private String summary;

    private String category;

    private Integer price;

    private String keywords; 

    public static BookSummaryRepository repository() {
        BookSummaryRepository bookSummaryRepository = AiconnectApplication.applicationContext.getBean(
            BookSummaryRepository.class
        );
        return bookSummaryRepository;
    }

}
//>>> DDD / Aggregate Root
