package factory;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import repository.*;
import validator.RepositoryException;
import validator.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Singleton Factory class responsible for creating and configuring repository instances.
 * It reads the application settings from the "config.properties" file and instantiates
 * the appropriate repository implementation (Memory, File, or Database) based on those settings.
 */
public class RepositoryFactory {
    private static RepositoryFactory instance = null;
    private final Properties properties;

    private RepositoryFactory() {
        properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RepositoryException("The config.properties file was not found!");
            }
            properties.load(is);
        } catch (IOException e) {
            throw new RepositoryException("Error reading the configuration file", e);
        }
    }

    /**
     * Retrieves the single instance of the RepositoryFactory.
     * If the instance does not exist, it is created and the configuration properties are loaded.
     *
     * @return the singleton instance of RepositoryFactory
     * @throws RepositoryException if the "config.properties" file cannot be found or read during initialization
     */
    public static RepositoryFactory getInstance() {
        if (instance == null) {
            instance = new RepositoryFactory();
        }
        return instance;
    }

    private RepositoryType getRepositoryType() {
        String typeStr = properties.getProperty("repository.type");
        if (typeStr == null) {
            throw new RepositoryException("The repository.type property is missing from config.properties");
        }
        try {
            return RepositoryType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Repository type '" + typeStr + "' is invalid. Allowed values: MEMORY, FILE, DATABASE");
        }
    }

    /**
     * Creates a User repository based on the configured repository type.
     *
     * @param validator the Validator used to validate User entities before storage
     * @return a specific implementation of Repository for managing users
     * @throws RepositoryException if the required configuration properties are missing or the repository type is unknown
     */
    public Repository<Long, User> createUserRepository(Validator<User> validator) {
        RepositoryType type = getRepositoryType();

        Map<RepositoryType, Function<Validator<User>, Repository<Long, User>>> repositorySupplierMap = Map.of(
                RepositoryType.MEMORY, InMemoryRepository::new,
                RepositoryType.FILE, val -> {
                    String file = properties.getProperty("users.file");
                    if (file == null || file.isBlank()) throw new RepositoryException("The users.file property is missing!");
                    return new UserFileRepository(val, file);
                },
                RepositoryType.DATABASE, val -> {
                    String url = properties.getProperty("db.url");
                    String user = properties.getProperty("db.user");
                    String password = properties.getProperty("db.password");

                    if (url == null || url.isBlank() || user == null || user.isBlank() || password == null || password.isBlank()) {
                        throw new RepositoryException("Database configuration properties (url, user, password) are missing!");
                    }
                    return new UserDbRepository(val, url, user, password);
                }
        );

        Function<Validator<User>, Repository<Long, User>> factoryFunction = repositorySupplierMap.get(type);
        if (factoryFunction == null) {
            throw new RepositoryException("Unknown repository type: " + type);
        }
        return factoryFunction.apply(validator);
    }

    /**
     * Creates a Friendship repository based on the configured repository type.
     *
     * @param validator the Validator used to validate Friendship entities before storage
     * @return a specific implementation of Repository for managing friendships
     * @throws RepositoryException if the required configuration properties are missing or the repository type is unknown
     */
    public Repository<Tuple<Long, Long>, Friendship> createFriendshipRepository(Validator<Friendship> validator) {
        RepositoryType type = getRepositoryType();

        Map<RepositoryType, Function<Validator<Friendship>, Repository<Tuple<Long, Long>, Friendship>>> repositoryFunctionMap = Map.of(
                RepositoryType.MEMORY, InMemoryRepository::new,
                RepositoryType.FILE, val -> {
                    String file = properties.getProperty("friendships.file");
                    if (file == null || file.isBlank()) throw new RepositoryException("The friendships.file property is missing!");
                    return new FriendshipFileRepository(val, file);
                },
                RepositoryType.DATABASE, val -> {
                    String url = properties.getProperty("db.url");
                    String user = properties.getProperty("db.user");
                    String password = properties.getProperty("db.password");

                    if (url == null || url.isBlank() || user == null || user.isBlank() || password == null || password.isBlank()) {
                        throw new RepositoryException("Database configuration properties (url, user, password) are missing!");
                    }
                    return new FriendshipDbRepository(val, url, user, password);
                }
        );

        Function<Validator<Friendship>, Repository<Tuple<Long, Long>, Friendship>> factoryFunction = repositoryFunctionMap.get(type);
        if (factoryFunction == null) {
            throw new RepositoryException("Unknown repository type: " + type);
        }
        return factoryFunction.apply(validator);
    }
}