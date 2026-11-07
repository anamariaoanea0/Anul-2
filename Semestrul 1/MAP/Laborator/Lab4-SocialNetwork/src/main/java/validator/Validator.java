package validator;

/**
 * Strategy interface for validating entities.
 *
 * @param <T> the type of the entity to be validated
 */
@FunctionalInterface
public interface Validator<T>{
    /**
     * Validates the given entity.
     *
     * @param entity the entity to validate
     * @throws ValidationException if the entity is not valid
     */
    void validate(T entity) throws ValidationException;

    /**
     * Combines this validator with another one.
     *
     * @param other the next validator to apply
     * @return a composed validator
     */
    default Validator<T> andThen(Validator<T> other) {
        if (other == null) {
            throw new IllegalArgumentException("The other validator cannot be null!");
        }
        return entity -> {
            this.validate(entity);
            other.validate(entity);
        };
    }
}
