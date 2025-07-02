package untitled.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import untitled.ReaderApplication;
import untitled.domain.ReaderJoined;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "ManagerReader_table")
@Data
//<<< DDD / Aggregate Root
public class ManagerReader {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private Boolean isSubscribe;

    private Boolean isKT;

    @PostPersist
    public void onPostPersist() {
        ReaderJoined readerJoined = new ReaderJoined(this);
        BeanUtils.copyProperties(this, readerJoined);
        readerJoined.publishAfterCommit();
    }

    public static ManagerReaderRepository repository() {
        ManagerReaderRepository managerReaderRepository = ReaderApplication.applicationContext.getBean(
            ManagerReaderRepository.class
        );
        return managerReaderRepository;
    }
    // PasswordEncoder에 접근하기 위한 static 메서드
    public static PasswordEncoder passwordEncoder() {
        return ReaderApplication.applicationContext.getBean(PasswordEncoder.class);
    }

    public void login() {
        //
    }
     public static ManagerReader createReader(String email, String rawPassword, String name, PasswordEncoder passwordEncoder) {
        if (email == null || rawPassword == null || name == null) {
            throw new IllegalArgumentException("필수 정보가 누락되었습니다.");
        }

        ManagerReader reader = new ManagerReader();
        reader.setEmail(email);
        reader.setName(name);
        
        // 비밀번호를 암호화하여 설정
        reader.setPassword(passwordEncoder.encode(rawPassword));
        
        reader.setIsSubscribe(false); // 기본 구독 상태 설정

        return reader;
    }
}
//>>> DDD / Aggregate Root
