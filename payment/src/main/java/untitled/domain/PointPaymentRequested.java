package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PointPaymentRequested extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Integer point;
    private String impUid;
    private Integer cost; // 결제 금액
}
