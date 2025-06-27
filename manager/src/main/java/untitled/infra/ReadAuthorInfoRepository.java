package untitled.infra;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import untitled.domain.*;

@RepositoryRestResource(
    collectionResourceRel = "readAuthorInfos",
    path = "readAuthorInfos"
)
public interface ReadAuthorInfoRepository
    extends PagingAndSortingRepository<ReadAuthorInfo, Long> {
    List<ReadAuthorInfo> findByAuthorId(Long authorId);
}
