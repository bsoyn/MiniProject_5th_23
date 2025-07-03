package untitled.domain.aggregate;

import javax.persistence.*;
import lombok.Data;

import untitled.dto.RequestPublicationDTO; 
import untitled.dto.TempSaveManuscriptDTO;


import untitled.domain.aggregate.Manuscript;
import untitled.domain.event.PublicationRequested;
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

    public void requestPublication(RequestPublicationDTO dto) {
        this.authorId = dto.getAuthorId();
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.status = ManuscriptStatus.SUBMITTED;
    }

    /**
     * 원고 제출 처리 (SUBMITTED 상태로 변경)
     */
    public void submit(String title, String contents) {
        this.title = title;
        this.contents = contents;
        this.status = ManuscriptStatus.SUBMITTED;
    }

    /**
     * 임시 저장 처리 (TEMP 상태로 변경)
     */
    public void tempSave(TempSaveManuscriptDTO dto) {
        this.authorId = dto.getAuthorId();
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.status = ManuscriptStatus.TEMP;
    }

    /**
     * 요약 생성 반영
     */
    public void applySummary(String summary, String category, Integer price) {
        this.summary = summary;
        this.category = category;
        this.price = price;
    }

    /**
     * 표지 이미지 반영
     */
    public void applyCover(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 도서 등록 완료 처리 (REGISTERED 상태로 변경)
     */
    public void markAsRegistered() {
        this.status = ManuscriptStatus.REGISTERED;
    }

}
