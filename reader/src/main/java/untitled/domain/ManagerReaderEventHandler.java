package untitled.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler // 이 클래스가 Repository 이벤트를 처리함을 선언합니다.
@RequiredArgsConstructor
public class ManagerReaderEventHandler {

    private final PasswordEncoder passwordEncoder;
    private final ManagerReaderRepository repository;

    // ManagerReader 엔티티가 생성되기 직전에 이 메소드가 자동으로 호출됩니다.
    @HandleBeforeCreate
    public void handleManagerReaderCreate(ManagerReader managerReader) {
        // 1. 이메일 중복 확인
        if (repository.findByEmail(managerReader.getEmail()).isPresent()) {
            // REST API에서 자동으로 적절한 에러 응답(예: 409 Conflict)을 생성합니다.
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        // 사용자가 POST로 보낸 평문 비밀번호를 가져와 암호화한 후 다시 설정합니다.
        String encodedPassword = passwordEncoder.encode(managerReader.getPassword());
        managerReader.setPassword(encodedPassword);
    }
}