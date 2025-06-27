package untitled.infra;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import untitled.domain.*;

@RepositoryRestResource(
    collectionResourceRel = "availiableBookLists",
    path = "availiableBookLists"
)
public interface AvailiableBookListRepository
    extends PagingAndSortingRepository<AvailiableBookList, Long> {
    List<AvailiableBookList> findByBookid(Long bookid);

    void deleteByReaderid(Long readerid);
}
