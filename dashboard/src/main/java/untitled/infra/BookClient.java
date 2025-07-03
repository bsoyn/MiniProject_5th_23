package untitled.infra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "books")
public interface BookClient {
    @GetMapping("/books/{id}")
    BookDto getBook(@PathVariable("id") Long id);
}
