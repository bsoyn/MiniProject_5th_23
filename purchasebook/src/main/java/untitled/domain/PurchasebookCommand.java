package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class PurchasebookCommand {

    private Long id;
    private Long readerId;
    private Long bookId;
}
