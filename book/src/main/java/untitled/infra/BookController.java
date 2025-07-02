package untitled.infra;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import untitled.domain.Book.BookService;
import untitled.domain.BookAccess.BookAccessService;
import untitled.domain.BookAccess.RequestbookAuthorityCommand;
//<<< Clean Arch / Inbound Adaptor
import org.springframework.web.bind.annotation.GetMapping;


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

    
    @GetMapping("/{bookId}")
    public BookInfo getBookInfo(@PathVariable Long bookId){

        return bookService.getBookInfo(bookId);
    }

    @GetMapping("/page/{pageNum}")
    public BookInfoList getBookInfoList(@PathVariable int pageNum){

        return bookService.getBookInfoList(pageNum);
    }

    @PostMapping(value = "/authority", produces = MediaType.APPLICATION_JSON_VALUE)
    public String requestBookAuthority(
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
