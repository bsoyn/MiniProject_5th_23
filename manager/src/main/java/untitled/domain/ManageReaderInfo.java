package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import untitled.ManagerApplication;

@Entity
@Table(name = "ManageReaderInfo_table")
@Data
//<<< DDD / Aggregate Root
public class ManageReaderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String readerId;

    private String email;

    private String name;

    public static ManageReaderInfoRepository repository() {
        ManageReaderInfoRepository manageReaderInfoRepository = ManagerApplication.applicationContext.getBean(
            ManageReaderInfoRepository.class
        );
        return manageReaderInfoRepository;
    }
}
//>>> DDD / Aggregate Root
