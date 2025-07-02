package untitled.domain;

import javax.persistence.*;
import lombok.Data;
import untitled.ManagerApplication;
import untitled.domain.AuthorApproved;
import untitled.domain.AuthorDenied;
import untitled.domain.File;

@Entity
@Table(name = "ManageAuthor_table")
@Data
public class ManageAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


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

    public static void registerNewAuthor(RegisterRequested registerRequested) {
        ManageAuthor manageAuthor = new ManageAuthor();

        manageAuthor.setName(registerRequested.getName());
        manageAuthor.setEmail(registerRequested.getEmail());
        manageAuthor.setBio(registerRequested.getBio());
        manageAuthor.setMajorWork(registerRequested.getMajorWork());
        manageAuthor.setPortfolio(registerRequested.getPortfolio());
        manageAuthor.setIsApproval(false);

        repository().save(manageAuthor);
    }
}
