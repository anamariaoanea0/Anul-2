package repository;

import domain.User;
import validator.RepositoryException;
import validator.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import util.paging.Page;
import util.paging.Pageable;

/**
 * Database repository implementation for managing User entities.
 */
public class UserDbRepository implements PagingRepository<Long, User>{
    private final Validator<User> validator;
    private final String url;
    private final String user;
    private final String password;

    /**
     * Constructs a new UserDbRepository with the required database connection details.
     *
     * @param validator the Validator used to validate User entities before saving
     * @param url the JDBC URL used to connect to the database
     * @param user the database username
     * @param password the database password
     */
    public UserDbRepository(Validator<User> validator, String url, String user, String password){
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Retrieves a User entity based on its ID.
     *
     * @param id the unique ID of the user to retrieve, must not be null
     * @return an Optional containing the found User entity, or an empty Optional if it does not exist
     * @throws IllegalArgumentException if the provided id is null
     * @throws RepositoryException if a database access error occurs
     */
    @Override
    public Optional<User> findOne(Long id){
        if(id == null){
            throw new IllegalArgumentException("The ID cannot be null!");
        }

        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    int age = resultSet.getInt("age");

                    User foundUser = new User(id, firstName, lastName, age);
                    return Optional.of(foundUser);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Database error when searching for user with ID: " + id, e);
        }

        return Optional.empty();
    }

    /**
     * Retrieves all User entities stored in the database.
     *
     * @return an Iterable containing all users from the database
     * @throws RepositoryException if a database access error occurs
     */
    @Override
    public Iterable<User> findAll(){

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                int age = resultSet.getInt("age");

                User u = new User(id, firstName, lastName, age);
                users.add(u);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error retrieving all users from the database", e);
        }

        return users;
    }

    /**
     * Saves a given User entity into the database.
     * If the user is successfully saved, the database-generated ID is assigned to the entity.
     *
     * @param u the User entity to be saved, must not be null
     * @return an empty Optional if the entity was saved successfully, or an Optional containing the entity if it already exists
     * @throws IllegalArgumentException if the provided user is null
     * @throws validator.ValidationException if the user fails validation checks
     * @throws RepositoryException if a database access error occurs
     */
    @Override
    public Optional<User> save(User u){

        if (u == null) {
            throw new IllegalArgumentException("The user cannot be null!");
        }
        validator.validate(u);

        if (u.getId() != null && findOne(u.getId()).isPresent()) {
            return Optional.of(u);
        }

        String sql = "INSERT INTO users (first_name, last_name, age) VALUES (?, ?, ?) RETURNING id";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, u.getFirstName());
            statement.setString(2, u.getLastName());
            statement.setInt(3, u.getAge());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Long generatedId = resultSet.getLong("id");
                    u.setId(generatedId);
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error saving the user to the database", e);
        }

        return Optional.of(u);
    }

    /**
     * Deletes a User entity from the database based on its ID.
     *
     * @param id the unique ID of the user to be deleted, must not be null
     * @return an Optional containing the deleted User entity, or an empty Optional if no user with the given ID existed
     * @throws IllegalArgumentException if the provided id is null
     * @throws RepositoryException if a database access error occurs
     */
    @Override
    public Optional<User> delete(Long id){
        if (id == null) {
            throw new IllegalArgumentException("The ID cannot be null!");
        }

        Optional<User> userToDelete = findOne(id);
        if (userToDelete.isEmpty()) {
            return Optional.empty();
        }

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

            return userToDelete;

        } catch (SQLException e) {
            throw new RepositoryException("Error deleting user with ID: " + id, e);
        }
    }

    /**
     * Counts the total number of users in the database.
     *
     * @param connection the active database connection
     * @return the total number of users
     * @throws SQLException if a database access error occurs
     */
    private int count(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            if (result.next()) {
                return result.getInt("count");
            }
            return 0;
        }
    }

    /**
     * Retrieves a single page of users from the database using the given connection.
     *
     * @param connection the active database connection
     * @param pageable the pagination parameters (page number and page size)
     * @return a list of User entities on the requested page
     * @throws SQLException if a database access error occurs
     */
    private List<User> findAllOnPage(Connection connection, Pageable pageable) throws SQLException {
        List<User> usersOnPage = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id LIMIT ? OFFSET ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, pageable.getPageSize() * pageable.getPageNumber());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    int age = resultSet.getInt("age");

                    usersOnPage.add(new User(id, firstName, lastName, age));
                }
            }
        }
        return usersOnPage;
    }

    /**
     * Retrieves a page of User entities along with the total number of users in the database.
     *
     * @param pageable the pagination request, must not be null
     * @return a Page containing the users on the requested page and the total user count
     * @throws IllegalArgumentException if pageable is null
     * @throws RepositoryException if a database access error occurs
     */
    @Override
    public Page<User> findAllOnPage(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable must not be null!");
        }

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            int totalNumberOfUsers = count(connection);
            List<User> usersOnPage;
            if (totalNumberOfUsers > 0) {
                usersOnPage = findAllOnPage(connection, pageable);
            } else {
                usersOnPage = new ArrayList<>();
            }
            return new Page<>(usersOnPage, totalNumberOfUsers);
        } catch (SQLException e) {
            throw new RepositoryException("Error retrieving users page from the database", e);
        }
    }
}
