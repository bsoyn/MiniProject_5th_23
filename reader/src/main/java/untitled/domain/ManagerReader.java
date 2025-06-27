package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import untitled.ReaderApplication;
import untitled.domain.ReaderJoined;

@Entity
@Table(name = "ManagerReader_table")
@Data
//<<< DDD / Aggregate Root
public class ManagerReader {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String password;

    private String name;

    private Boolean isSubscribe;

    @PostPersist
    public void onPostPersist() {
        ReaderJoined readerJoined = new ReaderJoined(this);
        readerJoined.publishAfterCommit();
    }

    public static ManagerReaderRepository repository() {
        ManagerReaderRepository managerReaderRepository = ReaderApplication.applicationContext.getBean(
            ManagerReaderRepository.class
        );
        return managerReaderRepository;
    }

    public void login() {
        //
    }
}
//>>> DDD / Aggregate Root
