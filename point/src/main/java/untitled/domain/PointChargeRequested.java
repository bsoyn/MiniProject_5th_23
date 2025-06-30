package untitled.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointChargeRequested {
    private Long readerId;
    private Integer point;
    private String impUid;
    private Integer cost;
}
