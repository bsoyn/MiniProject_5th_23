package untitled.domain;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.*;

@Component
@Data
public class IamportService {

    @Value("${iamport.api-key}")
    private String apiKey;

    @Value("${iamport.api-secret}")
    private String apiSecret;

    public IamportClient createClient() {
        return new IamportClient(apiKey, apiSecret);
    }
}
