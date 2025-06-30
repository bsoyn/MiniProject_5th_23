package untitled.domain.Book;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import untitled.infra.BookRepository;;

@Service
public class BookService {
    
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public void requestBookRegistrationAlert(WritingCompleted writingCompleted) {

        Book book = new Book();
        book.setTitle(writingCompleted.getTitle());
        book.setAuthorId(writingCompleted.getAuthorId());
        book.setContents(writingCompleted.getContents());
        book.setSummary(writingCompleted.getSummary());
        book.setImageUrl(writingCompleted.getImageUrl());
        book.setCategory(writingCompleted.getCategory());
        book.setPrice(writingCompleted.getPrice());

        bookRepository.save(book);

        BookRegistered bookRegistered = new BookRegistered(book);
        bookRegistered.setManuscriptId(writingCompleted.getManuscriptId());

        bookRegistered.publishAfterCommit();
    }


}
