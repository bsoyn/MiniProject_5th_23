package untitled.domain;

import javax.persistence.*;
import lombok.Data;
import untitled.AuthorApplication;
import untitled.domain.AuthorInfoUpdated;
import untitled.domain.UpdateCommand;
import untitled.domain.RegisterCommand;
import untitled.domain.AuthorDenied;
import untitled.domain.AuthorApproved;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "Author_table")
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String password;
    private String name;
    private String bio;
    private String majorWork;

    @Embedded
    private File portfolio;

    private Boolean isApproval;

    // 기본 생성자
    public Author() {
        this.isApproval = false; // 기본값 설정
    }

    @PreUpdate
    public void onPreUpdate() {
        AuthorInfoUpdated authorInfoUpdated = new AuthorInfoUpdated(this);
        authorInfoUpdated.publishAfterCommit();
    }

    @PrePersist
    public void onPrePersist() {
        // 등록 요청 이벤트 발행
        RegisterRequested registerRequested = new RegisterRequested(this);
        registerRequested.publishAfterCommit();
    }

    public static AuthorRepository repository() {
        AuthorRepository authorRepository = AuthorApplication.applicationContext.getBean(
            AuthorRepository.class
        );
        return authorRepository;
    }
    
    // PasswordEncoder에 접근하기 위한 static 메서드
    public static PasswordEncoder passwordEncoder() {
        return AuthorApplication.applicationContext.getBean(PasswordEncoder.class);
    }

    // Spring Data REST를 위한 setter 메서드들
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        // Repository Event Handler에서 암호화됨
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setMajorWork(String majorWork) {
        this.majorWork = majorWork;
    }

    public void setPortfolio(File portfolio) {
        this.portfolio = portfolio;
    }

    public void setIsApproval(Boolean isApproval) {
        this.isApproval = isApproval;
    }

    // 기존 메서드들 유지
    public void register(RegisterCommand registerCommand) {
        this.email = registerCommand.getEmail();
        this.password = registerCommand.getPassword(); // Repository Event Handler에서 암호화됨
        this.name = registerCommand.getName();
        this.bio = registerCommand.getBio();
        this.majorWork = registerCommand.getMajorWork();
        this.portfolio = registerCommand.getPortfolio();
        this.isApproval = false;
    }
    
    public static Author createAuthor(String email, String rawPassword, String name, String bio, String majorWork, File portfolio, PasswordEncoder passwordEncoder) {
        if (email == null || rawPassword == null || name == null) {
            throw new IllegalArgumentException("필수 정보가 누락되었습니다.");
        }

        Author author = new Author();
        author.setEmail(email);
        author.setName(name);
        author.setBio(bio);
        author.setMajorWork(majorWork);
        author.setPortfolio(portfolio);
        
        // 비밀번호를 암호화하여 설정
        author.setPassword(passwordEncoder.encode(rawPassword));
        
        author.setIsApproval(false); // 기본 승인 상태 설정

        return author;
    }

    public static void sendRejectAlert(AuthorDenied authorDenied) {
        repository().findAll().forEach(author -> {
            if (author.getEmail().equals(authorDenied.getEmail())) {
                System.out.println("Author " + author.getName() + " (ID: " + author.getId() + ") registration was denied.");
                System.out.println("Email notification sent to: " + author.getEmail());
            }
        });
    }

    public static void updateApprovalStatus(AuthorApproved authorApproved) {
        // authorId가 제거되었으므로 이메일로 작가를 찾아서 승인 상태 업데이트
        repository().findAll().forEach(author -> {
            if (author.getEmail().equals(authorApproved.getEmail())) {
                author.setIsApproval(true);
                repository().save(author);
                
                System.out.println("Author " + author.getName() + " (ID: " + author.getId() + ") has been approved.");
                System.out.println("Welcome email sent to: " + author.getEmail());
            }
        });
    }

    public void updateInfo(UpdateCommand updateCommand) {
        this.email = updateCommand.email;
        this.name = updateCommand.name;
        this.bio = updateCommand.bio;
        this.majorWork = updateCommand.majorWork;
        this.portfolio = updateCommand.portfolio;
    }
}
