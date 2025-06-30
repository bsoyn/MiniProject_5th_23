package untitled.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import untitled.domain.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping("/manageAuthors")
@Transactional
public class ManageAuthorController {

    @Autowired
    ManageAuthorRepository manageAuthorRepository;

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ManageAuthor> approve(@PathVariable Long id) {
        try {
            Optional<ManageAuthor> optional = manageAuthorRepository.findById(id);
            if (optional.isPresent()) {
                ManageAuthor author = optional.get();
                author.setIsApproval(true);
                ManageAuthor savedAuthor = manageAuthorRepository.save(author);

                // 승인 이벤트 발행
                AuthorApproved event = new AuthorApproved(author);
                event.publishAfterCommit();

                return ResponseEntity.ok(savedAuthor);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.err.println("Error approving author: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/deny")
    public ResponseEntity<ManageAuthor> deny(@PathVariable Long id) {
        try {
            Optional<ManageAuthor> optional = manageAuthorRepository.findById(id);
            if (optional.isPresent()) {
                ManageAuthor author = optional.get();
                author.setIsApproval(false);
                ManageAuthor savedAuthor = manageAuthorRepository.save(author);

                // 거부 이벤트 발행
                AuthorDenied event = new AuthorDenied(author);
                event.publishAfterCommit();

                return ResponseEntity.ok(savedAuthor);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.err.println("Error denying author: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 작가 목록 조회: GET /manageAuthors
    @GetMapping
    public ResponseEntity<Iterable<ManageAuthor>> getAllAuthors() {
        try {
            Iterable<ManageAuthor> authors = manageAuthorRepository.findAll();
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            System.err.println("Error retrieving authors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 특정 작가 조회: GET /manageAuthors/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ManageAuthor> getAuthor(@PathVariable Long id) {
        try {
            Optional<ManageAuthor> optional = manageAuthorRepository.findById(id);
            if (optional.isPresent()) {
                return ResponseEntity.ok(optional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.err.println("Error retrieving author: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
