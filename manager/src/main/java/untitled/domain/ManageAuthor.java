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

    @PostUpdate
    public void onPostUpdate() {
        AuthorApproved authorApproved = new AuthorApproved(this);
        authorApproved.publishAfterCommit();

        AuthorDenied authorDenied = new AuthorDenied(this);
        authorDenied.publishAfterCommit();
    }

    public static ManageAuthorRepository repository() {
        ManageAuthorRepository manageAuthorRepository = ManagerApplication.applicationContext.getBean(
            ManageAuthorRepository.class
        );
        return manageAuthorRepository;
    }

    //<<< Clean Arch / Port Method
    public static void registerNewAuthor(RegisterRequested registerRequested) {
        //implement business logic here:

        /** Example 1:  new item 
        ManageAuthor manageAuthor = new ManageAuthor();
        repository().save(manageAuthor);

        */

        /** Example 2:  finding and process
        

        repository().findById(registerRequested.get???()).ifPresent(manageAuthor->{
            
            manageAuthor // do something
            repository().save(manageAuthor);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
