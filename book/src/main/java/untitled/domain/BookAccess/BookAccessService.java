package untitled.domain.BookAccess;

import org.springframework.stereotype.Service;

import untitled.infra.BookRepository;
import untitled.infra.BookAccessRepository;
import untitled.domain.BookAccess.*;

@Service
public class BookAccessService {
    
    private final BookRepository bookRepository;
    private final BookAccessRepository bookAccessRepositoty;

    public BookAccessService(BookRepository bookRepository, BookAccessRepository bookAccessRepository){
        this.bookRepository = bookRepository;
        this.bookAccessRepositoty = bookAccessRepository;
    }

    public void requestbookAuthority(
        RequestbookAuthorityCommand requestbookAuthorityCommand
    ) {
        BooKAccessRequested bookAccessRequested = new BooKAccessRequested();
        bookAccessRequested.setBookId(requestbookAuthorityCommand.getBookId());
        bookAccessRequested.setReaderId(requestbookAuthorityCommand.getReaderId());
        bookAccessRequested.publishAfterCommit();
    }

    public void bookAccessApproveAlert(
        SubscriptionValidChecked subscriptionValidChecked
    ) {
        bookRepository.findById(subscriptionValidChecked.getBookId()).ifPresent(book->{
            
            
            BookAccessApproved bookAccessApproved = new BookAccessApproved();
            bookAccessApproved.setReaderId(subscriptionValidChecked.getReaderId());

            bookAccessApproved.publishAfterCommit();
         });

    }

    public void bookAccessApproveAlert(
        PurchaseBookConfirmed purchaseBookConfirmed
    ) { 
        
        bookRepository.findById(purchaseBookConfirmed.getBookId()).ifPresent(book->{

            BookAccessApproved bookAccessApproved = new BookAccessApproved();
            
            bookAccessApproved.setIsPurchased(true);
            bookAccessApproved.setReaderId(purchaseBookConfirmed.getReaderId());

            bookAccessApproved.publishAfterCommit();
         });

    }

    public static void bookAccessDeniedAlert(
        SubscriptionFinished subscriptionFinished
    ) {
        
    }

    public static void bookAccessDeniedAlert(
        NotParchaseBookConfirmed notParchaseBookConfirmed
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Book book = new Book();
        repository().save(book);

        BookAccessRejected bookAccessRejected = new BookAccessRejected(book);
        bookAccessRejected.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(notParchaseBookConfirmed.get???()).ifPresent(book->{
            
            book // do something
            repository().save(book);

            BookAccessRejected bookAccessRejected = new BookAccessRejected(book);
            bookAccessRejected.publishAfterCommit();

         });
        */

    }
    
}
