package untitled.infra;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import untitled.domain.*;

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
