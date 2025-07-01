// domain/ReaderService.java
package untitled.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import untitled.infra.dto.RegisterRequestDTO;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderService {

    private final ManagerReaderRepository readerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional // 데이터베이스에 변경을 가하므로 트랜잭션 처리
    public void register(RegisterRequestDTO requestDto) {
        // 1. 이메일 중복 확인
        if (readerRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 3. 엔티티 객체 생성
        ManagerReader newReader = new ManagerReader();
        newReader.setEmail(requestDto.getEmail());
        newReader.setPassword(encodedPassword);
        newReader.setName(requestDto.getName());
        //기본 값 = kt아님, 구독안함
        int isKT = Optional.ofNullable(requestDto.getIsKT()).orElse(0);
        newReader.setIsKT(isKT);
        boolean isSubscribed = Optional.ofNullable(requestDto.getIsSubscribe()).orElse(false);
        newReader.setIsSubscribe(isSubscribed);

        // 4. 데이터베이스에 저장
        readerRepository.save(newReader);
    }
}