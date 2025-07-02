package untitled.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import untitled.dto.ValidationRequest;
import untitled.dto.ValidationResponse;

@FeignClient(name = "author", url = "http://localhost:8082")
public interface AuthorServiceClient {
    @PostMapping(value = "/internal/validate", consumes = "application/json")
    ValidationResponse validate(ValidationRequest request);
}
