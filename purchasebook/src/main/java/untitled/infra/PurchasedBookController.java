package untitled.infra;

import java.util.Optional;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import untitled.domain.*;
import untitled.service.PurchaseService;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/purchasedBooks")
@Transactional
public class PurchasedBookController {

    // @Autowired
    // PurchasedBookRepository purchasedBookRepository;

    @Autowired
    private PurchaseService purchaseService;   

    // POST: 구매 요청
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

        // 서비스에서 모든 로직 처리
        return purchaseService.requestPurchase(purchasebookCommand);       
    }

    // GET: 독자의 전체 구매 이력 조회
    @GetMapping("/purchasedBooks/history/{readerId}")
    public List<PurchasedBook> getPurchaseHistory(@PathVariable Long readerId) {
        
        return purchaseService.getPurchaseHistory(readerId);
    }
}
//>>> Clean Arch / Inbound Adaptor
