package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.PaymentResponse;
import lombok.Data;
import org.springframework.context.ApplicationContext;

import javax.persistence.*;
import java.util.Optional;

import untitled.PaymentApplication;
import untitled.domain.PaymentFailed;
import untitled.domain.PaymentFinished;

@Entity
@Table(name = "Payment_table")
@Data
//<<< DDD / Aggregate Root
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long readerId;

    private Integer point;

    private Integer cost;

    private Boolean isCompleted;

    public static PaymentRepository repository() {
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(
            PaymentRepository.class
        );
        return paymentRepository;
    }

    // 여기서 결제 성공하면, 완료 해서 보내고, 실패하면, 실패로 보내고..
    //<<< Clean Arch / Port Method
    public static void pointpayment(
        PointPaymentRequested event
    ) {
        
        // Payment 객체 생성 및 초기값 설정
        Payment payment = new Payment();
        payment.setReaderId(event.getReaderId());
        payment.setPoint(event.getPoint());
        payment.setCost(event.getCost());

        try {
            // 아임포트 클라이언트 생성
            IamportService iamportService = PaymentApplication.applicationContext.getBean(IamportService.class);
            IamportClient client = iamportService.createClient();

            // 결제 정보 조회
            IamportResponse<PaymentResponse> response = client.paymentByImpUid(event.getImpUid());

            if (response.getResponse().getAmount().intValue() == event.getCost()) {
                // 결제 성공
                payment.setIsCompleted(true);
                repository().save(payment);

                PaymentFinished finished = new PaymentFinished(payment);
                finished.setId(payment.getId());
                finished.setReaderId(payment.getReaderId());
                finished.setPoint(payment.getPoint());
                finished.setCost(payment.getCost());
                finished.setIsCompleted(true);
                finished.publishAfterCommit();
            } else {
                // 금액 불일치 → 실패 처리
                payment.setIsCompleted(false);
                repository().save(payment);

                PaymentFailed failed = new PaymentFailed(payment);
                failed.setId(payment.getId());
                failed.setReaderId(payment.getReaderId());
                failed.setPoint(payment.getPoint());
                failed.setCost(payment.getCost());
                failed.setIsCompleted(false);
                failed.publishAfterCommit();
            }
        } catch (Exception e) {
            // 예외 발생 → 실패 처리
            payment.setIsCompleted(false);
            repository().save(payment);

            PaymentFailed failed = new PaymentFailed(payment);
            failed.setId(payment.getId());
            failed.setReaderId(payment.getReaderId());
            failed.setPoint(payment.getPoint());
            failed.setCost(payment.getCost());
            failed.setIsCompleted(false);
            failed.publishAfterCommit();
        }

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
