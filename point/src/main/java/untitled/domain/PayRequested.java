package untitled.domain;
 
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;
 
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PayRequested extends AbstractEvent {
 
    // 구독권에서 받아오는 거
    private Long id;
    private Long readerId;
    // private Date subscribeStartDate;
    // private Date subscribeEndDate;
}