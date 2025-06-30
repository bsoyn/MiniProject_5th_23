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
import org.springframework.beans.BeanUtils;

import org.springframework.beans.BeanUtils;

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

    @Lob
    private String content;

    @Lob
    @Column(columnDefinition = "TEXT")
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

    //<<< Clean Arch / Port Method
    public void requestPublication(
        RequestPublicationCommand requestPublicationCommand
    ) {
            
            this.authorId = requestPublicationCommand.getAuthorId();
            this.title = requestPublicationCommand.getTitle();
            this.content = requestPublicationCommand.getContent();

            repository().save(this);

            PublicationRequested publicationRequested = new PublicationRequested(
                this
            );
            publicationRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void alertSummaryCreated(SummaryCreated summaryCreated) {
        repository().findById(summaryCreated.getId()).ifPresent(manuscript -> {
            manuscript.setSummary(summaryCreated.getSummary());
            manuscript.setCategory(summaryCreated.getCategory());
            manuscript.setPrice(summaryCreated.getPrice());

            repository().save(manuscript);
    });

    }

    public static void alertCoverCreated(CoverCreated coverCreated) {
        repository().findById(coverCreated.getManuscriptId()).ifPresent(manuscript -> {
            manuscript.setImageUrl(coverCreated.getImageUrl());
            repository().save(manuscript);

            WritingCompleted writingCompleted = new WritingCompleted(manuscript);
            writingCompleted.publishAfterCommit();
        });

    }

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
