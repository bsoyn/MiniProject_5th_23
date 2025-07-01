package untitled.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;
import untitled.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "subscribes",
    path = "subscribes"
)
public interface SubscribeRepository
    extends PagingAndSortingRepository<Subscribe, Long> {

         Optional<Subscribe> findByReaderId(Long readerId);
    }
