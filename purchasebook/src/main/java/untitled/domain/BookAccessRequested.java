package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;

@Data
public class BookAccessRequested {
    private Long readerId;
    private Long bookId;
}
