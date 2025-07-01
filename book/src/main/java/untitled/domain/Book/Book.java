package untitled.domain.Book;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Book_table")
@Data
//<<< DDD / Aggregate Root
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private Long authorId;

    @Lob
    private String contents;

    private String summary;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    private String category;

    private Integer price;

    private Long views;

}
//>>> DDD / Aggregate Root
