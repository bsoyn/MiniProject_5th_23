package untitled.domain;

import javax.persistence.*;
import lombok.Data;
import untitled.AuthorApplication;
import untitled.domain.AuthorInfoUpdated;
import untitled.domain.UpdateCommand;
import untitled.domain.RegisterCommand;
import untitled.domain.AuthorDenied;
import untitled.domain.AuthorApproved;

@Entity
@Table(name = "Author_table")
@Data
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

    public void register(RegisterCommand registerCommand) {
        this.email = registerCommand.getEmail();
        this.name = registerCommand.getName();
        this.bio = registerCommand.getBio();
        this.majorWork = registerCommand.getMajorWork();
        this.portfolio = registerCommand.getPortfolio();
        this.isApproval = false;
    }

    public static void sendRejectAlert(AuthorDenied authorDenied) {
        repository().findAll().forEach(author -> {
            if (author.getEmail().equals(authorDenied.getEmail())) {
                System.out.println("Author " + author.getName() + " (ID: " + author.getId() + ") registration was denied.");
                System.out.println("Email notification sent to: " + author.getEmail());
            }
        });
    }

    public static void updateApprovalStatus(AuthorApproved authorApproved) {
        // authorId가 제거되었으므로 이메일로 작가를 찾아서 승인 상태 업데이트
        repository().findAll().forEach(author -> {
            if (author.getEmail().equals(authorApproved.getEmail())) {
                author.setIsApproval(true);
                repository().save(author);
                
                System.out.println("Author " + author.getName() + " (ID: " + author.getId() + ") has been approved.");
                System.out.println("Welcome email sent to: " + author.getEmail());
            }
        });
    }

    public void updateInfo(UpdateCommand updateCommand) {
        this.email = updateCommand.email;
        this.name = updateCommand.name;
        this.bio = updateCommand.bio;
        this.majorWork = updateCommand.majorWork;
        this.portfolio = updateCommand.portfolio;
    }
}
