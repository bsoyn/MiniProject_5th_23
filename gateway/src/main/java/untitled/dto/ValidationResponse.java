package untitled.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationResponse {
    private boolean isValid;
    private String userId;
}