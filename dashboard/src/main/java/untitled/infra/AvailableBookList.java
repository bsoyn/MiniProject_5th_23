package untitled.infra;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import untitled.domain.AvailableBookView;

@Getter
@AllArgsConstructor
public class AvailableBookList {
    List<AvailableBookInfo> bookInfoList;

    public static AvailableBookList of(List<AvailableBookView> views){
        return new AvailableBookList(
            views.stream().map(AvailableBookInfo::of).collect(Collectors.toList())
        );
    }
}
