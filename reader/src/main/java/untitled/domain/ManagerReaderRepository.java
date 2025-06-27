package untitled.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import untitled.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "managerReaders",
    path = "managerReaders"
)
public interface ManagerReaderRepository
    extends PagingAndSortingRepository<ManagerReader, Long> {}
