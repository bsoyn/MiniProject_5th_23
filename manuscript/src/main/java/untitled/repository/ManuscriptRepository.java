package untitled.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import untitled.domain.aggregate.Manuscript;
import untitled.domain.aggregate.ManuscriptStatus;
//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "manuscripts",
    path = "manuscripts"
)
public interface ManuscriptRepository
    extends PagingAndSortingRepository<Manuscript, Long> {

        List<Manuscript> findByAuthorIdAndStatus(Long authorId, ManuscriptStatus status);

    }
