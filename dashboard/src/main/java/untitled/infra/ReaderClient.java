package untitled.infra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reader", url = "http://reader-service:8082")
public interface ReaderClient {

    @GetMapping("/managerReader/{id}")
    ReaderDto getReader(@PathVariable("id") Long id);
}
