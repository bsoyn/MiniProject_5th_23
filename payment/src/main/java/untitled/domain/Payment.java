package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
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
        PointPaymentRequested pointPaymentRequested
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Payment payment = new Payment();
        repository().save(payment);

        PaymentFinished paymentFinished = new PaymentFinished(payment);
        paymentFinished.publishAfterCommit();
        PaymentFailed paymentFailed = new PaymentFailed(payment);
        paymentFailed.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(pointPaymentRequested.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);

            PaymentFinished paymentFinished = new PaymentFinished(payment);
            paymentFinished.publishAfterCommit();
            PaymentFailed paymentFailed = new PaymentFailed(payment);
            paymentFailed.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
