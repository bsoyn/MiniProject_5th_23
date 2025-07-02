package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class RequestPublicationCommand {
    private Long id;
    private Long authorId;
    private String title;
    private String contents;
    private String imageUrl;
}
