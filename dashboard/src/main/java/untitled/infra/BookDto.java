package untitled.infra;

import lombok.Getter;

@Getter
public class BookDto {
    private Long bookId;
    private String title;
    private String summary;
    private String authorName;
    private String imageUrl;
    private String category;
    private String contents;
}
