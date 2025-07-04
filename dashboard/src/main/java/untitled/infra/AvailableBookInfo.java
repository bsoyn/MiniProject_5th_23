package untitled.infra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import untitled.domain.AvailableBookView;

@Getter
@AllArgsConstructor
public class AvailableBookInfo {
    private Long id;

    private Long bookId;
    private String title;
    private String authorName;
    private String summary;
    private String imageUrl;
    private String category;
    private String contents;

    private Long readerId;
    private String readerName;

    private Boolean isPurchased;

    public static AvailableBookInfo of(AvailableBookView view){
        return new AvailableBookInfo(
            view.getId(),
            view.getBookId(),
            view.getTitle(),
            view.getAuthorName(),
            view.getSummary(),
            view.getImageUrl(),
            view.getCategory(),
            view.getContents(),
            view.getReaderId(),
            view.getReaderName(),
            view.getIsPurchased());
    }
}
