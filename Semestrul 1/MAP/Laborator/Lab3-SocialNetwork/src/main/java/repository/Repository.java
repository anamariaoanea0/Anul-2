package repository;

import domain.Entity;
import validator.ValidationException;

public interface Repository<ID, E extends Entity<ID>> {
    /**
     * Finds an entity by its id.
     *
     * @param id the id of the entity to be returned, id must not be null
     * @return the entity with the specified id or null if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    E findOne(ID id);

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
     * @return null if the given entity is saved, otherwise returns the entity (if an entity with the same id already exists)
     * @throws ValidationException if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     */
    E save(E entity) throws ValidationException;

    /**
     * Removes the entity with the specified id.
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    E delete(ID id);
}
