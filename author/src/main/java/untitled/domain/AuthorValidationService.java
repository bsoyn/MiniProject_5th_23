package untitled.domain;

import untitled.infra.dto.ValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorValidationService {

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;

    public ValidationResponse validate(String email, String password) {
        return authorRepository.findByEmail(email)
            .filter(author -> passwordEncoder.matches(password, author.getPassword()))
            .filter(author -> author.getIsApproval() != null && author.getIsApproval()) // 승인된 작가만 로그인 가능
            .map(author -> new ValidationResponse(true, author.getId().toString(), author.getName()))
            .orElse(new ValidationResponse(false, null, null));
    }
} 