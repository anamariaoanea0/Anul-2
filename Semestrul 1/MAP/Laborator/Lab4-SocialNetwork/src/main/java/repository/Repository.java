package repository;

import domain.Entity;
import validator.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface Repository<ID, E extends Entity<ID>> {
    /**
     * Finds an entity by its id.
     *
     * @param id the id of the entity to be returned, id must not be null
     * @return an Optional containing the entity with the specified id, or an empty Optional if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    Optional<E> findOne(ID id);

    /**
     * Retrieves all entities.
     *
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     * Saves the given entity.
     *
     * @param entity entity must be not null
     * @return an empty Optional if the given entity is saved, otherwise returns an Optional containing the entity (if an entity with the same id already exists)
     * @throws ValidationException if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     */
    Optional<E> save(E entity) throws ValidationException;

    /**
     * Removes the entity with the specified id.
     *
     * @param id id must be not null
     * @return an Optional containing the removed entity, or an empty Optional if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    Optional<E> delete(ID id);

    default List<E> findAll(Predicate<E> filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Filter must not be null");
        }
        return StreamSupport.stream(findAll().spliterator(), false)
                .filter(filter)
                .collect(Collectors.toList());
    }
}
