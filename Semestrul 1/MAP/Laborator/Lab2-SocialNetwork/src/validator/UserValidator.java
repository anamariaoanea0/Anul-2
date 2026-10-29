package validator;

import domain.User;

/**
 * Validator implementation for the User entity.
 * Ensures that a user object meets the required business rules before being processed or saved.
 */
public class UserValidator implements Validator<User>{

    /**
     * Validates the given User entity.
     *
     * @param entity the User entity to be validated
     * @throws ValidationException if the user fails any of the validation checks
     */
    @Override
    public void validate(User entity) throws ValidationException {
        String errorMessage = "";

        if (entity.getFirstName() == null || entity.getFirstName().trim().isEmpty()) {
            errorMessage += "First name cannot be null or empty!";
        }

        if (entity.getLastName() == null || entity.getLastName().trim().isEmpty()) {
            errorMessage += "Last name cannot be null or empty!";
        }

        if (entity.getAge() < 14) {
            errorMessage += "The minimum age is 14 years.";
        }

        if (!errorMessage.isEmpty()) {
            throw new ValidationException(errorMessage);
        }
    }
}
