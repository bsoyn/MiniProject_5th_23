package untitled.domain.Book;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import untitled.infra.BookRepository;
import untitled.infra.BookInfo;
import untitled.infra.BookInfoList;

@Service
public class BookService {
    
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public void requestBookRegistrationAlert(WritingCompleted writingCompleted) {

        if(bookRepository.findByManuscriptId(writingCompleted.getManuscriptId()).isEmpty()){
            Book book = new Book();
            book.setTitle(writingCompleted.getTitle());
            book.setAuthorId(writingCompleted.getAuthorId());
            book.setAuthorName(writingCompleted.getAuthorName());
            book.setContents(writingCompleted.getContents());
            book.setSummary(writingCompleted.getSummary());
            book.setImageUrl(writingCompleted.getImageUrl());
            book.setCategory(writingCompleted.getCategory());
            book.setPrice(writingCompleted.getPrice());
            book.setViews(0L);
            book.setManuscriptId(writingCompleted.getManuscriptId());

            bookRepository.save(book);

            BookRegistered bookRegistered = new BookRegistered(book);

            bookRegistered.publishAfterCommit();
        }else{
            System.out.println("이미 출판된 책입니다.");
        }
    }

    public BookInfo getBookInfo(Long bookId){
        Book book = bookRepository.findById(bookId).orElseThrow();

        return BookInfo.of(book);
    }

    public BookInfoList getBookInfoList(int pageNum){
        Pageable pageable = PageRequest.of(pageNum, 10); // 10개씩

        Page<Book> page = bookRepository.findAll(pageable);
        List<Book> books = page.getContent();

        return BookInfoList.of(books);
    }


}
