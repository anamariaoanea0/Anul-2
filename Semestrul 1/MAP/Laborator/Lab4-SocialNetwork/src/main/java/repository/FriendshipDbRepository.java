package repository;

import domain.Friendship;
import domain.Tuple;
import validator.RepositoryException;
import validator.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Database repository implementation for managing Friendship entities.
 */
public class FriendshipDbRepository implements Repository<Tuple<Long, Long>, Friendship> {
    private final Validator<Friendship> validator;
    private final String url;
    private final String user;
    private final String password;

    /**
     * Constructs a new FriendshipDbRepository with the required database connection details.
     *
     * @param validator the Validator used to validate Friendship entities before saving
     * @param url the JDBC URL used to connect to the database
     * @param user the database username
     * @param password the database password
     */
    public FriendshipDbRepository(Validator<Friendship> validator, String url, String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Retrieves a Friendship entity based on its composite ID.
     *
     * @param id the composite ID of the friendship, represented as a Tuple of two User IDs, must not be null
     * @return an Optional containing the found Friendship entity, or an empty Optional if it does not exist
     * @throws IllegalArgumentException if the provided id is null
     * @throws RepositoryException if a database access error occurs
     */
    @Override
    public Optional<Friendship> findOne(Tuple<Long, Long> id) {
        if (id == null) {
            throw new IllegalArgumentException("The ID cannot be null!");
        }

        String sql = "SELECT * FROM friendships WHERE id1 = ? AND id2 = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id.getLeft());
            statement.setLong(2, id.getRight());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Timestamp timestamp = resultSet.getTimestamp("friends_from");
                    LocalDateTime friendsFrom = timestamp.toLocalDateTime();

                    Friendship friendship = new Friendship(id.getLeft(), id.getRight(), friendsFrom);
                    return Optional.of(friendship);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Database error while searching for friendship", e);
        }

        return Optional.empty();
    }

    /**
     * Retrieves all Friendship entities stored in the database.
     *
     * @return an Iterable containing all friendships from the database
     * @throws RepositoryException if a database access error occurs
     */
    @Override
    public Iterable<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();
        String sql = "SELECT * FROM friendships";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                LocalDateTime friendsFrom = resultSet.getTimestamp("friends_from").toLocalDateTime();

                Friendship friendship = new Friendship(id1, id2, friendsFrom);
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error retrieving all friendships from the database", e);
        }

        return friendships;
    }

    /**
     * Saves a given Friendship entity into the database.
     *
     * @param entity the Friendship entity to be saved, must not be null
     * @return an empty Optional if the entity was saved successfully, or an Optional containing the entity if it already exists
     * @throws IllegalArgumentException if the provided entity is null
     * @throws validator.ValidationException if the entity fails validation checks
     * @throws RepositoryException if a database access error occurs
     */
    @Override
    public Optional<Friendship> save(Friendship entity) {
        if (entity == null) {
            throw new IllegalArgumentException("The entity cannot be null!");
        }
        validator.validate(entity);

        if (entity.getId() != null && findOne(entity.getId()).isPresent()) {
            return Optional.of(entity);
        }

        String sql = "INSERT INTO friendships (id1, id2, friends_from) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, entity.getIdUser1());
            statement.setLong(2, entity.getIdUser2());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));

            statement.executeUpdate();
            return Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException("Error saving the friendship to the database", e);
        }
    }

    /**
     * Deletes a Friendship entity from the database based on its composite ID.
     *
     * @param id the composite ID of the friendship to be deleted, represented as a Tuple, must not be null
     * @return an Optional containing the deleted Friendship entity, or an empty Optional if no friendship with the given ID existed
     * @throws IllegalArgumentException if the provided id is null
     * @throws RepositoryException if a database access error occurs
     */
    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> id) {
        if (id == null) {
            throw new IllegalArgumentException("The ID cannot be null!");
        }

        Optional<Friendship> friendshipToDelete = findOne(id);
        if (friendshipToDelete.isEmpty()) {
            return Optional.empty();
        }

        String sql = "DELETE FROM friendships WHERE id1 = ? AND id2 = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id.getLeft());
            statement.setLong(2, id.getRight());

            statement.executeUpdate();
            return friendshipToDelete;

        } catch (SQLException e) {
            throw new RepositoryException("Error deleting friendship from the database", e);
        }
    }
}