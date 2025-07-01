package untitled.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import untitled.domain.*;
import untitled.infra.*;
import untitled.infra.dto.LoginRequest;
import untitled.infra.dto.RegisterRequestDTO;
import untitled.infra.dto.TokenResponse;
import untitled.config.*;

//<<< Clean Arch / Inbound Adaptor

@RepositoryRestController
@RequestMapping(value="/managerReaders")
@Transactional
public class ManagerReaderController {

    // @Autowired
    private final ManagerReaderRepository managerReaderRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ReaderService readerService;

    @Autowired
    public ManagerReaderController(ManagerReaderRepository readerRepository,
                                   PasswordEncoder passwordEncoder,
                                   JwtTokenProvider jwtTokenProvider,
                                   ReaderService readerService) {
        this.managerReaderRepository = readerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.readerService = readerService;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO requestDto) {
        try {
            readerService.register(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    // // --- 로그인 기능 추가 ---
    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    //     try {
    //         // 1. 이메일로 Reader 정보 조회
    //         ManagerReader reader = managerReaderRepository.findByEmail(request.getEmail())
    //                 .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    //         // 2. 비밀번호 일치 여부 확인
    //         if (!passwordEncoder.matches(request.getPassword(), reader.getPassword())) {
    //             throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    //         }

    //         // 3. JWT 생성
    //         String accessToken = jwtTokenProvider.createAccessToken(
    //                 reader.getId().toString(),
    //                 "READER"
    //         );

    //         return ResponseEntity.ok(new TokenResponse(accessToken));

    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    //     }
    // }
}
//>>> Clean Arch / Inbound Adaptor
