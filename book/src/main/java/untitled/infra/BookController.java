package untitled.infra;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import untitled.domain.Book.Book;
import untitled.domain.Book.BookService;
import untitled.domain.BookAccess.BookAccess;
import untitled.domain.BookAccess.BookAccessService;
import untitled.domain.BookAccess.RequestbookAuthorityCommand;
//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value="/books")
@Transactional
public class BookController {

    private final BookService bookService;
    private final BookAccessService bookAccessService;
    
    public BookController(BookService bookService, BookAccessService bookAccessService){
        this.bookService = bookService;
        this.bookAccessService = bookAccessService;
    }

    @PostMapping(value = "/authority", produces = MediaType.APPLICATION_JSON_VALUE)
    public String requestBookAuthority(
        @PathVariable Long bookId,
        @RequestBody RequestbookAuthorityCommand requestbookAuthorityCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /books/authority called #####");

        bookAccessService.requestbookAuthority(requestbookAuthorityCommand);
        
        return "도서 권한 요청이 처리되었습니다.";
    }
    
}
//>>> Clean Arch / Inbound Adaptor
