package untitled.infra;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import untitled.domain.*;

@RepositoryRestResource(
    collectionResourceRel = "availableBookViews",
    path = "availableBookViews"
)
public interface AvailableBookViewRepository
    extends PagingAndSortingRepository<AvailableBookView, Long> {
    List<AvailableBookView> findByBookId(Long bookid);

    void deleteByReaderid(Long readerid);

    Optional<AvailableBookView> findByReaderidAndBookid(Long readerid, Long bookid);

    List<AvailableBookView> findByReaderId(Long readerId);

    @Transactional
    @Modifying
    long deleteByReaderidAndIsPurchasedFalse(Long readerid);
}
