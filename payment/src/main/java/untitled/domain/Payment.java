package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import untitled.PaymentApplication;

@Entity
@Table(name = "Payment_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long readerId;
    private Integer point;
    private Integer cost;
    private Boolean isCompleted;

    public static PaymentRepository repository() {
        return PaymentApplication.applicationContext.getBean(PaymentRepository.class);
    }

    public static void pointpayment(PointPaymentRequested event) {
        // Payment 도메인 객체 생성
        Payment payment = new Payment();
        payment.setReaderId(event.getReaderId());
        payment.setPoint(event.getPoint());
        payment.setCost(event.getCost());

        try {
            // 아임포트 클라이언트 생성
            IamportService iamportService = PaymentApplication.applicationContext.getBean(IamportService.class);
            IamportClient client = iamportService.createClient();

            // 결제 정보 조회
            IamportResponse<com.siot.IamportRestClient.response.Payment> response =
                    client.paymentByImpUid(event.getImpUid());

            com.siot.IamportRestClient.response.Payment iamportPayment = response.getResponse();

            if (iamportPayment.getAmount().intValue() == event.getCost()) {
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
}
