package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import java.io.File;      
import lombok.Data;
import untitled.infra.AbstractEvent;

@Data
public class BookAccessApproved extends AbstractEvent {

    private Long id;
    private Long bookId;
    private Long readerId;
    private Boolean isPurchased;
    private Boolean isSubscribed;

}
