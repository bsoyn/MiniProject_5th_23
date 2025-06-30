package untitled.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import untitled.domain.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/books")
@Transactional
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @RequestMapping(
        value = "/books/requestbookauthority",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Book requestbookAuthority(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RequestbookAuthorityCommand requestbookAuthorityCommand
    ) throws Exception {
        System.out.println("##### /book/requestbookAuthority  called #####");
       
        Book book = new Book();
        
        book.requestbookAuthority(requestbookAuthorityCommand);
        
        bookRepository.save(book);
        
        return book;
    }
}
//>>> Clean Arch / Inbound Adaptor
