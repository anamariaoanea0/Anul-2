package repository;

import domain.User;
import validator.RepositoryException;
import validator.Validator;

/**
 * File-based repository implementation for managing User entities.
 * Extends AbstractFileRepository to handle file reading and writing operations,
 * and includes automatic ID generation for new users.
 */
public class UserFileRepository extends AbstractFileRepository<Long, User> {

    /**
     * Constructs a new UserFileRepository and loads existing data from the specified file.
     *
     * @param validator the validator used to validate User entities before saving
     * @param fileName  the path to the file where users are stored
     */
    public UserFileRepository(Validator<User> validator, String fileName) {
        super(validator, fileName);
        loadData();
    }

    /**
     * Extracts a User entity from a formatted string line.
     *
     * @param line the string line read from the file
     * @return the User object parsed from the given string
     * @throws RepositoryException if the line does not contain exactly 4 fields, or if the ID or age cannot be parsed as numbers
     */
    @Override
    protected User extractEntity(String line) {
        String[] fields = line.split(";");
        if (fields.length != 4) {
            throw new RepositoryException("Incorrect number of fields for User");
        }
        try {
            Long id = Long.parseLong(fields[0]);
            String firstName = fields[1];
            String lastName = fields[2];
            int age = Integer.parseInt(fields[3]);
            return new User(id, firstName, lastName, age);
        } catch (NumberFormatException e) {
            throw new RepositoryException("Invalid format", e);
        }
    }

    /**
     * Converts a User entity into a formatted string for file storage.
     *
     * @param entity the User entity to be converted
     * @return a string representation of the entity in the format "id;firstName;lastName;age"
     */
    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName() + ";" + entity.getAge();
    }

    /**
     * Helper method to generate an auto-incremented ID for new users.
     *
     * @return the next available Long ID
     */
    private Long getNextId() {
        long maxId = 0L;
        for (User u : findAll()) {
            if (u.getId() > maxId) {
                maxId = u.getId();
            }
        }
        return maxId + 1;
    }

    /**
     * Saves a User entity to the repository.
     * If the entity's ID is null, an auto-incremented ID is generated and assigned before saving.
     *
     * @param entity the user to be saved
     * @return null if the entity was saved successfully, or the entity itself if a user with the same ID already exists
     */
    @Override
    public User save(User entity) {
        if (entity.getId() == null) {
            entity.setId(getNextId());
        }
        return super.save(entity);
    }
}