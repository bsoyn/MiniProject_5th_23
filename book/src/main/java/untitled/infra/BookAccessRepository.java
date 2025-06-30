package untitled.infra;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import untitled.domain.*;
import untitled.domain.BookAccess.BookAccess;

@RepositoryRestResource(collectionResourceRel = "bookAccesses", path = "books")
public interface BookAccessRepository extends PagingAndSortingRepository<BookAccess, Long> {}