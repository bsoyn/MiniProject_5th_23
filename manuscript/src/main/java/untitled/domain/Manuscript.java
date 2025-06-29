package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import untitled.ManuscriptApplication;
import untitled.domain.WritingCompleted;

import java.io.File;

@Entity
@Table(name = "Manuscript_table")
@Data
//<<< DDD / Aggregate Root
public class Manuscript {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long authorId;

    private String title;

    private String contentPath;

    private String imageUrl;

    private String summary;

    private String category;

    private Integer price;

    public static ManuscriptRepository repository() {
        ManuscriptRepository manuscriptRepository = ManuscriptApplication.applicationContext.getBean(
            ManuscriptRepository.class
        );
        return manuscriptRepository;
    }

    public void saveManuscript() {
        //
    }

    //<<< Clean Arch / Port Method
    public void requestPublication(
        RequestPublicationCommand requestPublicationCommand
    ) {
            this.authorId = requestPublicationCommand.getAuthorId();
            this.title = requestPublicationCommand.getTitle();
            this.contentPath = requestPublicationCommand.getContent().getPath();

        PublicationRequested publicationRequested = new PublicationRequested(
            this
        );
        publicationRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void alertSummaryCreated(SummaryCreated summaryCreated) {
        //implement business logic here:

        /** Example 1:  new item 
        Manuscript manuscript = new Manuscript();
        repository().save(manuscript);

        */

        /** Example 2:  finding and process
        
        // if summaryCreated.genAiId exists, use it
        
        // ObjectMapper mapper = new ObjectMapper();
        // Map<, Object> bookSummaryMap = mapper.convertValue(summaryCreated.getGenAiId(), Map.class);

        repository().findById(summaryCreated.get???()).ifPresent(manuscript->{
            
            manuscript // do something
            repository().save(manuscript);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void alertCoverCreated(CoverCreated coverCreated) {
        //implement business logic here:

        /** Example 1:  new item 
        Manuscript manuscript = new Manuscript();
        repository().save(manuscript);

        WritingCompleted writingCompleted = new WritingCompleted(manuscript);
        writingCompleted.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        // if coverCreated.genAiId exists, use it
        
        // ObjectMapper mapper = new ObjectMapper();
        // Map<, Object> bookCoverMap = mapper.convertValue(coverCreated.getGenAiId(), Map.class);

        repository().findById(coverCreated.get???()).ifPresent(manuscript->{
            
            manuscript // do something
            repository().save(manuscript);

            WritingCompleted writingCompleted = new WritingCompleted(manuscript);
            writingCompleted.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void alertBookRegistration(BookRegistered bookRegistered) {
        //implement business logic here:

        /** Example 1:  new item 
        Manuscript manuscript = new Manuscript();
        repository().save(manuscript);

        */

        /** Example 2:  finding and process
        

        repository().findById(bookRegistered.get???()).ifPresent(manuscript->{
            
            manuscript // do something
            repository().save(manuscript);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
