package repository;

import domain.Entity;
import util.paging.Page;
import util.paging.Pageable;

/**
 * Repository interface that extends standard CRUD operations with pagination support.
 *
 * @param <ID> the type of the entity identifier
 * @param <E>  the type of entities managed by this repository
 */
public interface PagingRepository<ID , E extends Entity<ID>> extends Repository<ID, E> {

    /**
     * Retrieves a page of entities based on the given pagination parameters.
     *
     * @param pageable the pagination request (page number and page size), must not be null
     * @return a Page containing the requested elements and the total number of elements
     * @throws IllegalArgumentException if pageable is null
     */
    Page<E> findAllOnPage(Pageable pageable);

}
