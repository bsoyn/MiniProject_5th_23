package untitled.domain.BookAccess;

import lombok.Data;

@Data
public class RequestbookAuthorityCommand {

    private Long readerId;
    private Long bookId;
}