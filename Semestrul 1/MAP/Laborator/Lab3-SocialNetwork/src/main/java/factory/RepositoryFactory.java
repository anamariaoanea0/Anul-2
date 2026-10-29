package factory;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import repository.*;
import validator.RepositoryException;
import validator.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
        switch (type) {
            case MEMORY:
                return new InMemoryRepository<>(validator);
            case FILE:
                String file = properties.getProperty("users.file");
                if (file == null) throw new RepositoryException("The users.file property is missing!");
                return new UserFileRepository(validator, file);
            case DATABASE:
                return new UserDbRepository(validator, properties.getProperty("db.url"), properties.getProperty("db.user"), properties.getProperty("db.password"));
            default:
                throw new RepositoryException("Unknown repository type!");
        }
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
        switch (type) {
            case MEMORY:
                return new InMemoryRepository<>(validator);
            case FILE:
                String file = properties.getProperty("friendships.file");
                if (file == null) throw new RepositoryException("The friendships.file property is missing!");
                return new FriendshipFileRepository(validator, file);
            case DATABASE:
                return new FriendshipDbRepository(validator, properties.getProperty("db.url"), properties.getProperty("db.user"), properties.getProperty("db.password"));
            default:
                throw new RepositoryException("Unknown repository type!");
        }
    }
}