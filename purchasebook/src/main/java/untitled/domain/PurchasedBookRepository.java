package untitled.domain;

import java.util.Optional;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import untitled.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "purchasedBooks",
    path = "purchasedBooks"
)
public interface PurchasedBookRepository
    extends PagingAndSortingRepository<PurchasedBook, Long> {

        // 1. 도서 구매 요청됨 <-> 구매 완료 알람/구매 실패 알람 사이 정보 확인
        Optional<PurchasedBook> findByReaderIdAndBookId(Long readerId, Long bookId);
        
        // 2. 구매한 도서인지 확인
        Optional<PurchasedBook> findByReaderIdAndBookIdAndStatus(Long readerId, Long bookId, String status);

        // 3. 독자의 전체 구매 이력 조회
        List<PurchasedBook> findByReaderIdAndStatus(Long readerId, String status);
}
