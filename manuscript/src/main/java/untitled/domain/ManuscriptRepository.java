package untitled.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import untitled.domain.*;

import java.util.List;
import untitled.domain.ManuscriptStatus;
import untitled.domain.Manuscript;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "manuscripts",
    path = "manuscripts"
)
public interface ManuscriptRepository
    extends PagingAndSortingRepository<Manuscript, Long> {

        List<Manuscript> findByAuthorIdAndStatus(Long authorId, ManuscriptStatus status);

    }
