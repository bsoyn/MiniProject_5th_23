package untitled.domain.BookAccess;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "BookAccess_table")
@Data
public class BookAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long bookId;

    private Long readerId;

    private Boolean isPurchased;

    private Boolean isSubscribed;

    private Boolean isPending;
}
