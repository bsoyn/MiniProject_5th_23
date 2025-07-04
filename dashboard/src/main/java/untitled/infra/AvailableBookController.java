package untitled.infra;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import untitled.domain.AvailableBookService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping(value="/availiableBookLists")
@Transactional
public class AvailableBookController {

    private final AvailableBookService availableBookService;
    
    public AvailableBookController(AvailableBookService availableBookService){
        this.availableBookService = availableBookService;
    }


    @GetMapping("/reader{currentUserId}")
    public AvailableBookList getAvailableBookListByReaderId(@PathVariable Long currentUserId) {
        
        return availableBookService.getAvailableBookList(currentUserId);
    }
    
    
}
