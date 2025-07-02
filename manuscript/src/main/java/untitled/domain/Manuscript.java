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
import untitled.domain.ManuscriptStatus;


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
    private String contents;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    private String summary;

    private String category;

    private Integer price;

    @Enumerated(EnumType.STRING)
    private ManuscriptStatus status;

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
            this.contents = requestPublicationCommand.getContents();
            this.status = ManuscriptStatus.SUBMITTED;

            repository().save(this);

            PublicationRequested publicationRequested = new PublicationRequested(
                this
            );
            publicationRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void alertSummaryCreated(SummaryCreated summaryCreated) {
        repository().findById(summaryCreated.getManuscriptId()).ifPresent(manuscript -> {
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
        });

    }

    public static void alertBookRegistration(BookRegistered bookRegistered) {

        repository().findById(bookRegistered.getManuscriptId()).ifPresent(manuscript->{
            
            System.out.println(
            "\n\n##### Manuscript : " +
            bookRegistered +
            "\n\n"
             );

            manuscript.setStatus(ManuscriptStatus.REGISTERED); 

            repository().save(manuscript);

         });
    

    }

    public void tempSave(TempSaveManuscriptCommand cmd) {
        this.authorId = cmd.getAuthorId();
        this.title = cmd.getTitle();
        this.contents = cmd.getContents();
        this.status = ManuscriptStatus.TEMP;

        repository().save(this);
    }


}
//>>> DDD / Aggregate Root
