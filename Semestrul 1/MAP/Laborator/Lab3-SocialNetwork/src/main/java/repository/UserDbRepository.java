package repository;

import domain.User;
import validator.Validator;

/**
 * Database-backed repository implementation for managing User entities.
 */
public class UserDbRepository implements Repository<Long, User>{

    /**
     * Constructs a new UserDbRepository with the required database connection details.
     *
     * @param validator the Validator used to validate User entities before saving
     * @param url       the JDBC URL used to connect to the database
     * @param user      the database username
     * @param password  the database password
     */
    public UserDbRepository(Validator<User> validator, String url, String user, String password){}

    /**
     * Retrieves a User entity based on its ID.
     *
     * @param id the unique ID of the user to retrieve
     * @return the found User entity, or null if it does not exist
     * @throws UnsupportedOperationException because the database integration is not yet implemented
     */
    @Override
    public User findOne(Long id){
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Retrieves all User entities stored in the database.
     *
     * @return an Iterable containing all users from the database
     * @throws UnsupportedOperationException because the database integration is not yet implemented
     */
    @Override
    public Iterable<User> findAll(){
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Saves a given User entity into the database.
     *
     * @param user the User entity to be saved
     * @return null if the entity was saved successfully, or the entity itself if it already exists
     * @throws UnsupportedOperationException because the database integration is not yet implemented
     */
    @Override
    public User save(User user){
        throw new UnsupportedOperationException(("Not implemented yet!"));
    }

    /**
     * Deletes a User entity from the database based on its ID.
     *
     * @param id the unique ID of the user to be deleted
     * @return the deleted User entity, or null if no user with the given ID existed
     * @throws UnsupportedOperationException because the database integration is not yet implemented
     */
    @Override
    public User delete(Long id){
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
