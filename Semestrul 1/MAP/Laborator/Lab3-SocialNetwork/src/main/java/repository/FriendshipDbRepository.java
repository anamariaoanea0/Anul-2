package repository;

import domain.Friendship;
import domain.Tuple;
import validator.Validator;

/**
 * Database-backed repository implementation for managing Friendship entities.
 */
public class FriendshipDbRepository implements Repository<Tuple<Long, Long>, Friendship> {

    /**
     * Constructs a new FriendshipDbRepository with the required database connection details.
     *
     * @param validator the Validator used to validate Friendship entities before saving
     * @param url       the JDBC URL used to connect to the database
     * @param user      the database username
     * @param password  the database password
     */
    public FriendshipDbRepository(Validator<Friendship> validator, String url, String user, String password) {
    }

    /**
     * Retrieves a Friendship entity based on its composite ID.
     *
     * @param id the composite ID of the friendship, represented as a Tuple of two User IDs
     * @return the found Friendship entity, or null if it does not exist
     * @throws UnsupportedOperationException because the database integration is not yet implemented
     */
    @Override
    public Friendship findOne(Tuple<Long, Long> id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Retrieves all Friendship entities stored in the database.
     *
     * @return an Iterable containing all friendships from the database
     * @throws UnsupportedOperationException because the database integration is not yet implemented
     */
    @Override
    public Iterable<Friendship> findAll() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Saves a given Friendship entity into the database.
     *
     * @param entity the Friendship entity to be saved
     * @return null if the entity was saved successfully, or the entity itself if it already exists
     * @throws UnsupportedOperationException because the database integration is not yet implemented
     */
    @Override
    public Friendship save(Friendship entity) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Deletes a Friendship entity from the database based on its composite ID.
     *
     * @param id the composite ID of the friendship to be deleted, represented as a Tuple
     * @return the deleted Friendship entity, or null if no friendship with the given ID existed
     * @throws UnsupportedOperationException because the database integration is not yet implemented
     */
    @Override
    public Friendship delete(Tuple<Long, Long> id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}