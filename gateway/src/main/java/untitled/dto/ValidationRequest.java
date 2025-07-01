package untitled.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationRequest {
    private String email;
    private String password;
}