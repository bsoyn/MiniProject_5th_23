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
import untitled.domain.AuthorApproved;
import untitled.domain.AuthorDenied;
import untitled.domain.File;

@Entity
@Table(name = "ManageAuthor_table")
@Data
//<<< DDD / Aggregate Root
public class ManageAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long authorId;

    private Boolean isApproval;

    private String name;

    private String bio;

    private String majorWork;

    @Embedded
    private File portfolio;

    private String email;

    public static ManageAuthorRepository repository() {
        ManageAuthorRepository manageAuthorRepository = ManagerApplication.applicationContext.getBean(
            ManageAuthorRepository.class
        );
        return manageAuthorRepository;
    }

    //<<< Clean Arch / Port Method
    public static void registerNewAuthor(RegisterRequested registerRequested) {
        // RegisterRequested 이벤트의 값으로 신규 작가 등록
        ManageAuthor manageAuthor = new ManageAuthor();
        manageAuthor.setAuthorId(registerRequested.getId());
        manageAuthor.setName(registerRequested.getName());
        manageAuthor.setEmail(registerRequested.getEmail());
        manageAuthor.setBio(registerRequested.getBio());
        manageAuthor.setMajorWork(registerRequested.getMajorWork());
        manageAuthor.setPortfolio(registerRequested.getPortfolio()); // 실제 portfolio 값 사용
        manageAuthor.setIsApproval(false); // 기본값: 미승인

        repository().save(manageAuthor);
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
