package untitled.infra; // 패키지 경로는 각 서비스에 맞게

import untitled.domain.ReaderValidationService;
import untitled.infra.dto.ValidationRequest;
import untitled.infra.dto.ValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/validate") // ★★★ Gateway의 FeignClient가 호출하는 경로
@RequiredArgsConstructor
public class InternalValidationController {

    private final ReaderValidationService validationService;

    @PostMapping
    public ResponseEntity<ValidationResponse> validateUser(@RequestBody ValidationRequest request) {
        ValidationResponse response = validationService.validate(request.getEmail(), request.getPassword());
        if (!response.isValid()) {
            // 사용자가 없거나 비밀번호가 틀리면 404 응답을 보내 Gateway가 알 수 있도록 함
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }
}