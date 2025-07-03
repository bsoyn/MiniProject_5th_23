package untitled.infra;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import untitled.domain.*;

// Spring Data REST를 사용하므로 Controller 제거
// Author 등록: POST /authors
// Author 조회: GET /authors
// Author 수정: PATCH /authors/{id}
// Author 삭제: DELETE /authors/{id}

@RestController
@RequestMapping("/authors")
@Transactional
public class AuthorController {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 승인된 작가만 조회
    @GetMapping
    public ResponseEntity<Iterable<Author>> getApprovedAuthors() {
        try {
            Iterable<Author> approvedAuthors = authorRepository.findByIsApprovalTrue();
            return ResponseEntity.ok(approvedAuthors);
        } catch (Exception e) {
            System.err.println("Error retrieving approved authors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 개별 작가 조회 (승인된 작가만)
    @GetMapping("/{id}")
    public ResponseEntity<Author> getApprovedAuthor(@PathVariable Long id) {
        try {
            Optional<Author> authorOpt = authorRepository.findById(id);
            if (authorOpt.isPresent()) {
                Author author = authorOpt.get();
                if (author.getIsApproval() != null && author.getIsApproval()) {
                    return ResponseEntity.ok(author);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.err.println("Error retrieving author: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 작가 등록 (비밀번호 암호화 포함)
    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        try {
            // 이메일 중복 확인
            if (authorRepository.findByEmail(author.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null); // 이미 사용 중인 이메일
            }

            // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(author.getPassword());
            author.setPassword(encodedPassword);
            
            author.setIsApproval(false); // 기본적으로 승인되지 않은 상태
            Author savedAuthor = authorRepository.save(author);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
        } catch (Exception e) {
            System.err.println("Error creating author: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 작가 정보 수정 (승인된 작가만)
    @PatchMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author authorUpdate) {
        try {
            Optional<Author> authorOpt = authorRepository.findById(id);
            if (authorOpt.isPresent()) {
                Author author = authorOpt.get();
                if (author.getIsApproval() != null && author.getIsApproval()) {
                    // 승인된 작가만 수정 가능
                    if (authorUpdate.getName() != null) author.setName(authorUpdate.getName());
                    if (authorUpdate.getBio() != null) author.setBio(authorUpdate.getBio());
                    if (authorUpdate.getMajorWork() != null) author.setMajorWork(authorUpdate.getMajorWork());
                    if (authorUpdate.getPortfolio() != null) author.setPortfolio(authorUpdate.getPortfolio());
                    
                    Author savedAuthor = authorRepository.save(author);
                    return ResponseEntity.ok(savedAuthor);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.err.println("Error updating author: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
