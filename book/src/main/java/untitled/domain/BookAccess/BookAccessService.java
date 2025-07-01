package untitled.domain.BookAccess;

import java.util.Optional;

import org.springframework.stereotype.Service;

import untitled.infra.BookRepository;
import untitled.infra.BookAccessRepository;
import untitled.domain.BookAccess.*;
import untitled.domain.Book.Book;

@Service
public class BookAccessService {
    
    private final BookRepository bookRepository;
    private final BookAccessRepository bookAccessRepository;

    public BookAccessService(BookRepository bookRepository, BookAccessRepository bookAccessRepository){
        this.bookRepository = bookRepository;
        this.bookAccessRepository = bookAccessRepository;
    }

    public void requestbookAuthority(
        RequestbookAuthorityCommand requestbookAuthorityCommand
    ) {
        Optional<Book> bookOpt = bookRepository.findById(requestbookAuthorityCommand.getBookId());
        if(bookOpt.isPresent()){
            BookAccess bookAccess = new BookAccess();
            bookAccess.setBookId(requestbookAuthorityCommand.getBookId());
            bookAccess.setReaderId(requestbookAuthorityCommand.getReaderId());
            bookAccess.setIsPurchased(false);
            bookAccess.setIsSubscribed(false);
            bookAccess.setIsPending(true);

            bookAccessRepository.save(bookAccess);

            BooKAccessRequested bookAccessRequested = new BooKAccessRequested(bookAccess);

            bookAccessRequested.publishAfterCommit();
        }else{
            System.out.println("책 정보를 찾을 수 가 없습니다.");
        }
    }

    public void bookAccessApproveAlert(
        SubscriptionValidChecked subscriptionValidChecked
    ) {
        Optional<BookAccess> bookAccessOpt = bookAccessRepository.findById(subscriptionValidChecked.getId());
        if(bookAccessOpt.isPresent()){
            BookAccess bookAccess = bookAccessOpt.get();
            bookAccess.setIsSubscribed(true);
            bookAccess.setIsPending(true);

            BookAccessApproved bookAccessApproved = new BookAccessApproved();
            bookAccessRepository.save(bookAccess);

            bookAccessApproved.publishAfterCommit();
        }else{
            System.out.println("요청 보낸 적 없음");
        }
    }

    public void bookAccessApproveAlert(
        PurchaseBookConfirmed purchaseBookConfirmed
    ) { 
        Optional<BookAccess> bookAccessOpt = bookAccessRepository.findById(purchaseBookConfirmed.getId());
        if(bookAccessOpt.isPresent()){
            BookAccess bookAccess = bookAccessOpt.get();
            bookAccess.setIsPurchased(true);
            bookAccess.setIsPending(true);

            BookAccessApproved bookAccessApproved = new BookAccessApproved();
            bookAccessRepository.save(bookAccess);

            bookAccessApproved.publishAfterCommit();
        }else{
            System.out.println("요청 보낸 적 없음");
        }
    }

    public void bookAccessDeniedAlert(
        SubscriptionFinished subscriptionFinished
    ) { 
        Optional<BookAccess> bookAccessOpt = bookAccessRepository.findById(subscriptionFinished.getId());
        if(bookAccessOpt.isPresent()){
            BookAccess bookAccess = bookAccessOpt.get();

            if(bookAccess.getIsPending()){
                if(!bookAccess.getIsSubscribed()){
                    BookAccessRejected bookAccessRejected = new BookAccessRejected(bookAccess);
                    bookAccessRejected.publishAfterCommit();
                }
            }else{
                bookAccess.setIsPending(true);
                bookAccessRepository.save(bookAccess);
            }

        }else{
            System.out.println("요청 보낸 적 없음");
        }
    }

    public void bookAccessDeniedAlert(
        NotParchaseBookConfirmed notParchaseBookConfirmed
    ) {
        Optional<BookAccess> bookAccessOpt = bookAccessRepository.findById(notParchaseBookConfirmed.getId());
        if(bookAccessOpt.isPresent()){
            BookAccess bookAccess = bookAccessOpt.get();

            if(bookAccess.getIsPending()){
                if(!bookAccess.getIsPurchased()){
                    BookAccessRejected bookAccessRejected = new BookAccessRejected(bookAccess);
                    bookAccessRejected.publishAfterCommit();
                }
            }else{
                bookAccess.setIsPending(true);
                bookAccessRepository.save(bookAccess);
            }
            
        }else{
            System.out.println("요청 보낸 적 없음");
        }

    }
    
}
