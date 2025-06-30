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
@RequestMapping("/authors")
@Transactional
public class AuthorController {

    @Autowired
    AuthorRepository authorRepository;

    // 신규 등록: POST /authors
    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Author> createAuthor(@RequestBody RegisterCommand registerCommand) {
        System.out.println("##### /authors POST called #####");
        try {
            Author author = new Author();
            author.register(registerCommand);
            Author savedAuthor = authorRepository.save(author);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
        } catch (Exception e) {
            System.err.println("Error creating author: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 정보 수정: PATCH /authors/{id}
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
    
    // 작가 조회: GET /authors/{id}
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
    
    // 전체 작가 목록 조회: GET /authors
    @GetMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Iterable<Author>> getAllAuthors() {
        try {
            Iterable<Author> authors = authorRepository.findAll();
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            System.err.println("Error retrieving authors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
