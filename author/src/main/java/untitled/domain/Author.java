package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import untitled.AuthorApplication;
import untitled.domain.AuthorInfoUpdated;
import untitled.domain.UpdateCommand;

@Entity
@Table(name = "Author_table")
@Data
//<<< DDD / Aggregate Root
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String name;

    private String bio;

    private String majorWork;

    @Embedded
    private File portfolio;

    private Boolean isApproval;

    @PreUpdate
    public void onPreUpdate() {
        AuthorInfoUpdated authorInfoUpdated = new AuthorInfoUpdated(this);
        authorInfoUpdated.publishAfterCommit();
    }

    public static AuthorRepository repository() {
        AuthorRepository authorRepository = AuthorApplication.applicationContext.getBean(
            AuthorRepository.class
        );
        return authorRepository;
    }

    //<<< Clean Arch / Port Method
    public void register(RegisterCommand registerCommand) {
        //implement business logic here:
        this.email = registerCommand.getEmail();
        this.name = registerCommand.getName();
        this.bio = registerCommand.getBio();
        this.majorWork = registerCommand.getMajorWork();
        this.portfolio = registerCommand.getPortfolio();
        this.isApproval = false; // 초기 상태는 승인되지 않음

        RegisterRequested registerRequested = new RegisterRequested(this);
        registerRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void sendRejectAlert(AuthorDenied authorDenied) {
        //implement business logic here:
        repository().findById(authorDenied.getAuthorId()).ifPresent(author -> {
            // 거부 알림 로직 구현
            System.out.println("Author " + author.getName() + " (ID: " + author.getId() + ") registration was denied.");
            System.out.println("Email notification sent to: " + author.getEmail());
            
            // 필요시 추가 처리 로직
            // 예: 이메일 발송, SMS 발송, 로그 기록 등
        });
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void updateApprovalStatus(AuthorApproved authorApproved) {
        //implement business logic here:
        repository().findById(authorApproved.getAuthorId()).ifPresent(author -> {
            author.setIsApproval(true);
            repository().save(author);
            
            System.out.println("Author " + author.getName() + " (ID: " + author.getId() + ") has been approved.");
            System.out.println("Welcome email sent to: " + author.getEmail());
            
            // 승인 후 추가 처리 로직
            // 예: 환영 이메일 발송, 작가 권한 부여 등
        });
    }
    //>>> Clean Arch / Port Method

    public void updateInfo(UpdateCommand updateCommand) {
        this.email = updateCommand.email;
        this.name = updateCommand.name;
        this.bio = updateCommand.bio;
        this.majorWork = updateCommand.majorWork;
        this.portfolio = updateCommand.portfolio;
    }

}
//>>> DDD / Aggregate Root
