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
// @RequestMapping(value="/purchasedBooks")
@Transactional
public class PurchasedBookController {

    @Autowired
    PurchasedBookRepository purchasedBookRepository;

    @RequestMapping(
        value = "/purchasedBooks/purchasebook",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public PurchasedBook purchasebook(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody PurchasebookCommand purchasebookCommand
    ) throws Exception {
        System.out.println("##### /purchasedBook/purchasebook  called #####");
        PurchasedBook purchasedBook = new PurchasedBook();
        purchasedBook.purchasebook(purchasebookCommand);
        purchasedBookRepository.save(purchasedBook);
        return purchasedBook;
    }
}
//>>> Clean Arch / Inbound Adaptor
