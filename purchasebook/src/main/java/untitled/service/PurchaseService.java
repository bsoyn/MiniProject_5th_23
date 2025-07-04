package untitled.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import untitled.domain.BookAccessRejected;
import untitled.domain.BookAccessRequested;
import untitled.domain.BuyApproved;
import untitled.domain.BuyRejected;
import untitled.domain.NotPurchaseBookConfirmed;
import untitled.domain.PurchaseBookConfirmed;
import untitled.domain.PurchaseFailed;
import untitled.domain.PurchasebookCommand;
import untitled.domain.PurchasedBook;
import untitled.domain.PurchasedBookRepository;
import untitled.domain.PurchasedCompleted;
import untitled.domain.SuggestPurchase;
import untitled.domain.PointPaymentRequested;

@Service
public class PurchaseService {

    @Autowired
    private PurchasedBookRepository repository;

    // '구매 요청됨' event 처리
    public PurchasedBook requestPurchase(PurchasebookCommand command) {
        PurchasedBook book = new PurchasedBook();
        book.setReaderId(command.getReaderId());
        book.setBookId(command.getBookId());
        book.setStatus("REQUESTED");
        book.setPrice(command.getPrice());

        repository.save(book); // @PostPersist에서 이벤트 발행됨
        return book;
    }   

    // 도서 구매 성공/실패 관련 policy
    public void handlePointPaymentRequested(PointPaymentRequested event) {
        // 'PointPaymentRequested' 이벤트로부터 readerId, bookId, 성공여부(isPurchase) 받아옴
        Long readerId = event.getReaderId();
        Long bookId = event.getBookId();
        boolean purchase = event.isPurchase();

        // 받은 이벤트가 구독권 구매 관련이 아니라, 도서 구매 관련인 경우
        if (bookId != null) {
            // 내가 구매를 요청한 도서인지 확인
            repository.findByReaderIdAndBookId(readerId, bookId)
                .ifPresent(book -> {
                    if (purchase == true) { // <구매 성공>
                        // 1. 상태 변경 후 정보 저장
                        book.markCompleted();
                        repository.save(book);

                        // 2. '구매 완료됨' 이벤트 발행
                        PurchasedCompleted completed = new PurchasedCompleted(book);
                        completed.publishAfterCommit();
                    } else { // <구매 실패>
                        // 1. 상태 변경 후 정보 저장
                        book.markFailed();
                        repository.save(book);

                        // 2. '구매 실패됨' 이벤트 발행
                        PurchaseFailed failed = new PurchaseFailed(book);
                        failed.publishAfterCommit();
                    }
                });            
        }
    }

    // // '도서 구매 완료 알림' policy
    // public void handleBuyApproved(BuyApproved event) {
    //     // 'BuyApproved' 이벤트로부터 readerId, bookId 받아옴
    //     Long readerId = event.getReaderId();
    //     Long bookId = event.getBookId();

    //     repository.findByReaderIdAndBookId(readerId, bookId)
    //         .ifPresent(book -> {
    //             // 1. 상태 변경 후 정보 저장
    //             book.markCompleted();
    //             repository.save(book);

    //             // 2. '구매 완료됨' 이벤트 발행
    //             PurchasedCompleted completed = new PurchasedCompleted(book);
    //             completed.publishAfterCommit();
    //         });
    // }

    // // '도서 구매 실패 알림' policy
    // public void handleBuyRejected(BuyRejected event) {
    //     // 'BuyApproved' 이벤트로부터 readerId, bookId 받아옴
    //     Long readerId = event.getReaderId();
    //     Long bookId = event.getBookId();

    //     repository.findByReaderIdAndBookId(readerId, bookId)
    //         .ifPresent(book -> {
    //             // 1. 상태 변경 후 정보 저장
    //             book.markFailed();
    //             repository.save(book);

    //             // 2. '구매 실패됨' 이벤트 발행
    //             PurchaseFailed failed = new PurchaseFailed(book);
    //             failed.publishAfterCommit();
    //         });

    // }

    // '구매한 도서인지 확인' policy
    public void handleBookAccessRequest(BookAccessRequested event) {
        Long id = event.getId();
        Long readerId = event.getReaderId();
        Long bookId = event.getBookId();

        Optional<PurchasedBook> purchasedOpt = repository.findByReaderIdAndBookIdAndStatus(readerId, bookId, "COMPLETED");

        if (purchasedOpt.isPresent()) {
            // 구매 내역이 있으면 'PurchaseBookConfirmed' 이벤트 발행
            PurchasedBook purchasedBook = purchasedOpt.get();
            PurchaseBookConfirmed confirmed = new PurchaseBookConfirmed(purchasedBook);
            confirmed.setId(event.getId());
            confirmed.publishAfterCommit();
        } else {
            // 구매 내역이 없으면 'NotPurchaseBookConfirmed' 이벤트 발행
            NotPurchaseBookConfirmed rejected = new NotPurchaseBookConfirmed(id, readerId, bookId);
            rejected.publishAfterCommit();
        }
    }

    // '구매 유도 알림' policy
    public void handleSuggestPurchase(BookAccessRejected event) {
        Long readerId = event.getReaderId();
        Long bookId = event.getBookId();

        SuggestPurchase suggest = new SuggestPurchase();
        suggest.setReaderId(readerId);
        suggest.setBookId(bookId);
        suggest.publishAfterCommit();
    }

    // 독자의 도서 구매 이력 조회
    public List<PurchasedBook> getPurchaseHistory(Long readerId) {
        return repository.findByReaderIdAndStatus(readerId, "COMPLETED");
    }

    // PurchaseService에 추가
    public boolean isPurchased(Long readerId, Long bookId) {
        // 구매 이력에서 해당 독자가 해당 도서를 구매했는지 확인
        return repository.existsByReaderIdAndBookId(readerId, bookId);
    }
}
