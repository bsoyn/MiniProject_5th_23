package untitled.infra;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import untitled.domain.*;
import java.time.LocalDate;

//<<< Clean Arch / Inbound Adaptor
@RestController
@RequestMapping(value="/subscribes")
@Transactional
public class SubscribeController {

    @Autowired
    SubscribeRepository subscribeRepository;

    // POST
    @PostMapping
    public ResponseEntity<String> create(@RequestBody Subscribe subscribe) {
        // readerId 받아서 PayRequested 이벤트를 발행
        if (subscribe.getReaderId() == null) {
            return ResponseEntity.badRequest().body("Reader ID is required.");
        }

        PayRequested payRequested = new PayRequested();
        payRequested.setReaderId(subscribe.getReaderId());
        payRequested.publishAfterCommit();

        return ResponseEntity.ok("Subscription request sent for readerId: " + subscribe.getReaderId());
    }

    @GetMapping
    public Iterable<Subscribe> list() {
        return subscribeRepository.findAll();
    }
    
    // GET
    @GetMapping("/reader/{readerId}")
    public ResponseEntity<?> getSubscribeByReaderId(@PathVariable Long readerId) {
        Subscribe subscribe = subscribeRepository.findByReaderId(readerId)
            .orElseThrow(() -> new RuntimeException("구독 내역이 없습니다."));

        Map<String, Object> response = new HashMap<>();
        response.put("readerId", subscribe.getReaderId());
        response.put("subscribeStartDate", subscribe.getSubscribeStartDate());
        response.put("subscribeEndDate", subscribe.getSubscribeEndDate());
        response.put("isSubscribed", subscribe.getSubscribeEndDate() != null &&
                            subscribe.getSubscribeEndDate().isAfter(LocalDate.now()));

        return ResponseEntity.ok(response);
    }

}
