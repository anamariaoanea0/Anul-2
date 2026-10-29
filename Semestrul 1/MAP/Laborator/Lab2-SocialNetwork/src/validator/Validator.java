package validator;

/**
 * Strategy interface for validating entities.
 *
 * @param <T> the type of the entity to be validated
 */
public interface Validator<T>{
    /**
     * Validates the given entity.
     *
     * @param entity the entity to validate
     * @throws ValidationException if the entity is not valid
     */
    void validate(T entity) throws ValidationException;
}
