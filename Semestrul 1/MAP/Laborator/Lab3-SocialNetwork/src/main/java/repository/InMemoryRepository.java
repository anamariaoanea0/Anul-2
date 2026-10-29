package repository;

import domain.Entity;
import validator.ValidationException;
import validator.Validator;

import java.util.HashMap;
import java.util.Map;

/**
 * A generic in-memory implementation of the Repository interface.
 * This class stores entities in a HashMap, meaning all data is lost when the application stops.
 *
 * @param <ID> the type of the unique identifier used by the entity
 * @param <E>  the type of the entity, which must extend the Entity class
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private final Validator<E> validator;
    protected final Map<ID, E> entities;

    /**
     * Constructs a new InMemoryRepository with the specified validator.
     * Initializes the internal storage as an empty HashMap.
     *
     * @param validator the Validator used to validate entities before they are stored
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        this.entities = new HashMap<>();
    }

    /**
     * Retrieves an entity based on its unique identifier.
     *
     * @param id the unique identifier of the entity to be found
     * @return the entity with the specified ID, or null if no such entity exists
     * @throws IllegalArgumentException if the provided ID is null
     */
    @Override
    public E findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }
        return entities.get(id);
    }

    /**
     * Retrieves all entities currently stored in the repository.
     *
     * @return an Iterable containing all the entities
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     * Saves a given entity to the repository.
     * The entity is first validated.
     * If an entity with the same ID already exists in the repository, the new entity is not saved.
     *
     * @param entity the entity to be saved
     * @return null if the entity was successfully saved, or the  entity itself
     *         if an entity with the same ID already exists in the repository
     * @throws IllegalArgumentException if the provided entity is null
     * @throws ValidationException if the entity fails validation checks
     */
    @Override
    public E save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        validator.validate(entity);

        if (entities.containsKey(entity.getId())) {
            return entity;
        }
        entities.put(entity.getId(), entity);
        return null;
    }

    /**
     * Deletes an entity from the repository based on its unique identifier.
     *
     * @param id the unique identifier of the entity to be deleted
     * @return the deleted entity, or null if no entity with the specified ID was found
     * @throws IllegalArgumentException if the provided ID is null
     */
    @Override
    public E delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }
        return entities.remove(id);
    }
}
