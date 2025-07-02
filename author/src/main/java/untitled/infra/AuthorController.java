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
@RequestMapping("/authors")
@Transactional
public class AuthorController {

    @Autowired
    AuthorRepository authorRepository;

    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Author> createAuthor(@RequestBody RegisterCommand registerCommand) {
        System.out.println("##### /authors POST called #####");
        try {
            Author author = new Author();
            author.register(registerCommand);
            Author savedAuthor = authorRepository.save(author);
            
            // 저장 후 이벤트 발행 (ID가 설정된 후)
            RegisterRequested registerRequested = new RegisterRequested(savedAuthor);
            registerRequested.publishAfterCommit();
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
        } catch (Exception e) {
            System.err.println("Error creating author: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Author> updateInfo(
        @PathVariable(value = "id") Long id,
        @RequestBody UpdateCommand updateCommand
    ) {
        try {
            Optional<Author> optionalAuthor = authorRepository.findById(id);
            if (optionalAuthor.isPresent()) {
                Author author = optionalAuthor.get();
                author.updateInfo(updateCommand);
                Author savedAuthor = authorRepository.save(author);
                return ResponseEntity.ok(savedAuthor);
            } else {
                throw new AuthorNotFoundException(id);
            }
        } catch (AuthorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            System.err.println("Error updating author: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Author> getAuthor(@PathVariable(value = "id") Long id) {
        try {
            Optional<Author> optionalAuthor = authorRepository.findById(id);
            if (optionalAuthor.isPresent()) {
                return ResponseEntity.ok(optionalAuthor.get());
            } else {
                throw new AuthorNotFoundException(id);
            }
        } catch (AuthorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            System.err.println("Error retrieving author: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Iterable<Author>> getAllAuthors() {
        try {
            // 승인받은 작가만 조회
            Iterable<Author> authors = authorRepository.findByIsApprovalTrue();
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            System.err.println("Error retrieving authors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
