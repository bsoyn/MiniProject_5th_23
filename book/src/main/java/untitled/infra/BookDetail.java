package untitled.infra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import untitled.domain.Book.Book;

@Getter
@AllArgsConstructor
public class BookDetail {
    private Long id;
    private String title;
    private Long authorId;
    private String authorName;
    private String contents;
    private String category;
    private Integer price;
    private String imageUrl;
    private String summary;
    private Long views;

    public static BookDetail of(Book book){
        return new BookDetail(
            book.getId(),
            book.getTitle(),
            book.getAuthorId(),
            book.getAuthorName(),
            book.getContents(),
            book.getCategory(),
            book.getPrice(),
            book.getImageUrl(),
            book.getSummary(),
            book.getViews()
        );
    }
}
