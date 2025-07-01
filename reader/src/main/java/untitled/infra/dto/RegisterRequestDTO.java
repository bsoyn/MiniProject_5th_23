package untitled.infra.dto; // 자신의 패키지 경로에 맞게 수정

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    private String email;
    private String password;
    private String name;
    private Boolean isSubscribe;
    private int isKT;
}