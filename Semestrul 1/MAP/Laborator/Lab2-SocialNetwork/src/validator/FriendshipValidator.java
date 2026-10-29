package validator;

import domain.Friendship;

/**
 * Validator implementation for the Friendship entity.
 * Ensures that a friendship object meets the required business rules before being processed or saved.
 */
public class FriendshipValidator implements Validator<Friendship>{

    /**
     * Validates the given Friendship entity.
     *
     * @param entity the Friendship entity to be validated
     * @throws ValidationException if the friendship fails the validation check
     */
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if (entity.getIdUser1().equals(entity.getIdUser2())) {
            throw new ValidationException("A user cannot be friends with themselves!");
        }
    }
}
