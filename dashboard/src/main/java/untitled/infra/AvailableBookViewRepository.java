package untitled.infra;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import untitled.domain.*;

@RepositoryRestResource(
    collectionResourceRel = "availableBookViews",
    path = "availableBookViews"
)
public interface AvailableBookViewRepository
    extends PagingAndSortingRepository<AvailableBookView, Long> {
    List<AvailableBookView> findByBookid(Long bookid);

    void deleteByReaderid(Long readerid);
}
