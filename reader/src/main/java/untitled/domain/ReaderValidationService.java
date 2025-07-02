package untitled.domain; // 패키지 경로는 각 서비스에 맞게

import untitled.infra.dto.ValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReaderValidationService {

    private final ManagerReaderRepository readerRepository;
    private final PasswordEncoder passwordEncoder;

    public ValidationResponse validate(String email, String password) {
        return readerRepository.findByEmail(email)
            .filter(reader -> passwordEncoder.matches(password, reader.getPassword()))
            .map(reader -> new ValidationResponse(true, reader.getId().toString()))
            .orElse(new ValidationResponse(false, null));
    }
}