package untitled.infra;

import lombok.AllArgsConstructor;
import untitled.domain.Book.Book;

@AllArgsConstructor
public class BookDetail {
    private Long id;
    private String title;
    private Long authorId;
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
            book.getContents(),
            book.getCategory(),
            book.getPrice(),
            book.getImageUrl(),
            book.getSummary(),
            book.getViews()
        );
    }
}
