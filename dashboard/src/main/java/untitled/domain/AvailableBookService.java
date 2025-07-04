package untitled.domain;

import java.util.List;

import org.springframework.stereotype.Service;

import untitled.infra.AvailableBookInfo;
import untitled.infra.AvailableBookList;
import untitled.infra.AvailableBookViewRepository;

@Service
public class AvailableBookService {
    
    private final AvailableBookViewRepository availableBookViewRepository;

    public AvailableBookService(AvailableBookViewRepository availableBookViewRepository){
        this.availableBookViewRepository = availableBookViewRepository;
    }

    public AvailableBookList getAvailableBookList(Long readerId){
        List<AvailableBookView> views = availableBookViewRepository.findByReaderId(readerId);
        return AvailableBookList.of(views);
    }
}
