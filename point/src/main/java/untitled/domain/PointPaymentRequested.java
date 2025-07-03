package untitled.domain;
 
import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;
 
// 책 구매!! 
// 포인트 결제랑 무관!!!
 
//<<< DDD / Domain Event
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PointPaymentRequested extends AbstractEvent {
 
    private Long id;
    private Long readerId;
    private Long bookId;
 
    // 해연님, 태현님에게 보낼 거
    // 추가될 수도.. ->> 결제 가격!
    // private Long price;
    private boolean purchase;
 
    public PointPaymentRequested(Point aggregate) {
        super(aggregate);
        this.readerId = aggregate.getReaderId();
    }
 
    // 서비스 코드 호환용 2-파라미터 버전
    public PointPaymentRequested(Long readerId, Long bookId) {
        super();
        this.readerId = readerId;
        this.bookId  = bookId;
    }
 
    // 3-파라미터 버전
    public PointPaymentRequested(Long readerId, Long bookId, boolean purchase) {
        this(readerId, bookId);
        this.purchase = purchase;
    }
}
//>>> DDD / Domain Event