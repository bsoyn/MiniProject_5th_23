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

        RegisterRequested registerRequested = new RegisterRequested(this);
        registerRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void sendRejectAlert(AuthorDenied authorDenied) {
        //implement business logic here:

        /** Example 1:  new item 
        Author author = new Author();
        repository().save(author);

        */

        /** Example 2:  finding and process
        

        repository().findById(authorDenied.get???()).ifPresent(author->{
            
            author // do something
            repository().save(author);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void updateApprovalStatus(AuthorApproved authorApproved) {
        //implement business logic here:

        /** Example 1:  new item 
        Author author = new Author();
        repository().save(author);

        */

        /** Example 2:  finding and process
        

        repository().findById(authorApproved.get???()).ifPresent(author->{
            
            author // do something
            repository().save(author);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
