package untitled.domain;
 
import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;
 
//<<< DDD / Domain Event
@Data
@ToString
@AllArgsConstructor
// 해연님, 태현님에게 받아온 거 저장하는 곳
public class RemainingPointChecked extends AbstractEvent {
 
    private Long id;
    private Long readerId;
    private Integer point; // 갖고 있는 point
    private Integer price; // 가격 (소모될)
 
    private Long bookId;

 
    public RemainingPointChecked(Point aggregate) {
        super(aggregate);
        this.readerId = aggregate.getReaderId();
        this.point = aggregate.getPoint();
    }
 
    public RemainingPointChecked() {
        super();
    }
}
//>>> DDD / Domain Event