package untitled.infra;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import untitled.domain.Book.Book;

@AllArgsConstructor
public class BookInfoList {
    private List<BookInfo> books;

    public static BookInfoList of(List<Book> books){
        return new BookInfoList(
            books.stream().map(BookInfo::of).collect(Collectors.toList())
        );
    }
}