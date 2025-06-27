package untitled.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import untitled.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "purchasedBooks",
    path = "purchasedBooks"
)
public interface PurchasedBookRepository
    extends PagingAndSortingRepository<PurchasedBook, Long> {}
