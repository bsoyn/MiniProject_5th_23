package untitled.domain;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import untitled.domain.*;

//<<< PoEAA / Repository
// Spring Data REST 비활성화 - 컨트롤러에서 직접 관리
// @RepositoryRestResource(collectionResourceRel = "authors", path = "authors")
public interface AuthorRepository
    extends PagingAndSortingRepository<Author, Long> {
    
    // 승인받은 작가만 조회
    Iterable<Author> findByIsApprovalTrue();
    
    // 이메일로 작가 조회 (로그인용)
    Optional<Author> findByEmail(String email);
}
