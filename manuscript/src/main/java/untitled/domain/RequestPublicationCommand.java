package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;
import java.io.File;

@Data
public class RequestPublicationCommand {
    private Long id;
    private Long authorId;
    private String title;
    private String content;
    private String imageUrl;
}
