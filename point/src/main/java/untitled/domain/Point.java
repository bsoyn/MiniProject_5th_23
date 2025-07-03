package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import untitled.PointApplication;
import untitled.domain.BuyApproved;
import untitled.domain.BuyRejected;
import untitled.domain.PointPaymentRequested;
import untitled.domain.RemainingPointChecked;

@Entity
@Table(name = "Point_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
//<<< DDD / Aggregate Root
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long readerId;

    private Integer point;

    private String impUid;

    private Integer cost;

    private Integer price;

    @PreUpdate
    public void onPreUpdate() {
        PointPaymentRequested pointPaymentRequested = new PointPaymentRequested(
            this
        );
        pointPaymentRequested.publishAfterCommit();
    }

    public static PointRepository repository() {
        PointRepository pointRepository = PointApplication.applicationContext.getBean(
            PointRepository.class
        );
        return pointRepository;
    }

    public static void givepoint(ReaderJoined readerJoined) {
        Point point = new Point();
        point.setReaderId(readerJoined.getId());

        int basePoint = (readerJoined.getIsKT() == true) ? 5000 : 1000;
        point.setPoint(basePoint);

        repository().save(point);

    }

    // 수정 완료  -> 결제 요청 보냈어! publish 했어!! 해연님
    public static void readRemainingPoint(PurchaseBookRequested purchaseBookRequested, Point point) {
        //implement business logic here:
        
        // 포인트로 도서 결제 요청 이벤트 발행
        RemainingPointChecked remainingPointChecked = new RemainingPointChecked();
        remainingPointChecked.setReaderId(purchaseBookRequested.getReaderId());
        remainingPointChecked.setPoint(point.getPoint());
        remainingPointChecked.setBookId(purchaseBookRequested.getBookId());
        remainingPointChecked.setPrice(purchaseBookRequested.getPrice());
        
        usePoint(remainingPointChecked);
        // remainingPointChecked.publish();
    }

    public static PointPaymentRequested usePoint(RemainingPointChecked remainingPointChecked) {
        Point point = repository().findByReaderId(remainingPointChecked.getReaderId())
            .orElseThrow(() -> new RuntimeException("포인트 계정 없음"));

        // price랑 point 비교 -> point가 더 많아. ::> point를 써야해
        PointPaymentRequested pointPaymentRequested;
        if (remainingPointChecked.getPoint() >= remainingPointChecked.getPrice()) {
            point.setPoint(point.getPoint() - remainingPointChecked.getPrice());
            repository().save(point);

            pointPaymentRequested = new PointPaymentRequested(remainingPointChecked.getReaderId(), remainingPointChecked.getBookId(), true);
        } else {
            pointPaymentRequested = new PointPaymentRequested(remainingPointChecked.getReaderId(), remainingPointChecked.getBookId(), false);
        }

        pointPaymentRequested.publish();
        return pointPaymentRequested;
    }


    public static void leadAdditionalBuyAlert(BuyRejected buyRejected) {
        RestTemplate restTemplate = new RestTemplate();

        // 수정해야하는 프론트 코드
        String frontUrl = "http://frontend-service/api/notification/failure"; 
        Map<String, Object> body = new HashMap<>();
        body.put("readerId", buyRejected.getReaderId());
        body.put("message", "도서 구매가 포인트 부족으로 실패했습니다.");
        body.put("reason", "NOT_ENOUGH_POINT");

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(frontUrl, body, String.class);
            System.out.println("프론트 전송 성공: " + response.getStatusCode());
        } catch (Exception e) {
            System.out.println("프론트 전송 실패: " + e.getMessage());
        }

    }

    // 태현님!
    public static void readRemainingPoint(PayRequested payRequested) {
        Point point = repository().findByReaderId(payRequested.getReaderId())
            .orElseThrow(() -> new RuntimeException("포인트 계정 없음"));

        // 담고 결제 이벤트 가야지!
        RemainingPointChecked remainingPointChecked = new RemainingPointChecked();
        remainingPointChecked.setReaderId(payRequested.getReaderId());
        remainingPointChecked.setPrice(9900); 
        remainingPointChecked.setPoint(point.getPoint());
        remainingPointChecked.setBookId(null); 
        usePoint(remainingPointChecked);
        // remainingPointChecked.publish();
    }


    public void buyPoint(BuyPointDto command) {
        this.readerId = command.getReaderId();
        this.point = command.getPoint();
        this.impUid = command.getImpUid();
        this.cost = command.getCost();

        // event driven
        PointChargeRequested event = new PointChargeRequested(this);

        event.publish();
    }


    // 포인트 충전
    public static void chargePoint(PaymentFinished paymentFinished) {
        Point point = repository().findByReaderId(paymentFinished.getReaderId())
            .orElseThrow(() -> new RuntimeException("포인트 계정이 없습니다."));

        Integer current = point.getPoint() != null ? point.getPoint() : 0;
        Integer added = paymentFinished.getPoint() != null ? paymentFinished.getPoint() : 0;

        point.setPoint(current + added); // 기존 + 새로 충전
        repository().save(point);

        // 결제 성공 여부에 관계없이 후속 알림 메시지 발행
        PointUsageRequested usageRequested = new PointUsageRequested();
        usageRequested.setReaderId(paymentFinished.getReaderId());
        usageRequested.setPoint(paymentFinished.getPoint());
        usageRequested.setPaymentId(paymentFinished.getId());
        usageRequested.setIsCompleted(true); // 결제 성공
        usageRequested.publishAfterCommit();
    }


    // 포인트 결제 실패 알림
    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void alertPayFailed(PaymentFailed paymentFailed) {
        // 결제 성공 여부에 관계없이 후속 알림 메시지 발행
        PointUsageRequested usageRequested = new PointUsageRequested();
        usageRequested.setReaderId(paymentFailed.getReaderId());
        usageRequested.setPoint(paymentFailed.getPoint());
        usageRequested.setPaymentId(paymentFailed.getId());
        usageRequested.setIsCompleted(false); // 결제 실패
        usageRequested.publish();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
