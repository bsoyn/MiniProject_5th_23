package untitled.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyPointDto{
    private Long readerId;
    private Integer point;
    private String impUid;
    private Integer cost;
}
