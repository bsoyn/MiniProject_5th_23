package untitled.domain;

import javax.persistence.*;
import lombok.Data;
import untitled.AuthorApplication;
import untitled.domain.AuthorInfoUpdated;
import untitled.domain.UpdateCommand;

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

        RegisterRequested registerRequested = new RegisterRequested(this);
        registerRequested.publishAfterCommit();
    }

    public static void sendRejectAlert(AuthorDenied authorDenied) {
        repository().findById(authorDenied.getAuthorId()).ifPresent(author -> {
            System.out.println("Author " + author.getName() + " (ID: " + author.getId() + ") registration was denied.");
            System.out.println("Email notification sent to: " + author.getEmail());
        });
    }

    public static void updateApprovalStatus(AuthorApproved authorApproved) {
        repository().findById(authorApproved.getAuthorId()).ifPresent(author -> {
            author.setIsApproval(true);
            repository().save(author);
            
            System.out.println("Author " + author.getName() + " (ID: " + author.getId() + ") has been approved.");
            System.out.println("Welcome email sent to: " + author.getEmail());
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
