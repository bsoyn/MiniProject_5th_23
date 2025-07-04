package untitled.infra;

import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;           // 추가 필요
import java.util.HashMap;       // 추가 필요

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


    // PurchasedBookController에 추가
    @GetMapping("/purchasedBooks/check/{readerId}/{bookId}")
    public ResponseEntity<Map<String, Boolean>> checkPurchase(
        @PathVariable Long readerId, 
        @PathVariable Long bookId
    ) {
        try {
            boolean isPurchased = purchaseService.isPurchased(readerId, bookId);
            
            Map<String, Boolean> response = new HashMap<>();
            response.put("isPurchased", isPurchased);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("구매 확인 중 오류: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Boolean> response = new HashMap<>();
            response.put("isPurchased", false);
            
            return ResponseEntity.ok(response); // 에러 시 false 반환
        }
}
}
//>>> Clean Arch / Inbound Adaptor
