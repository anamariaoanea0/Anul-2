package repository;

import domain.Friendship;
import domain.Tuple;
import validator.RepositoryException;
import validator.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * File-based repository implementation for managing Friendship entities.
 * Extends AbstractFileRepository to handle file reading and writing operations.
 */
public class FriendshipFileRepository extends AbstractFileRepository<Tuple<Long, Long>, Friendship> {

    /**
     * Constructs a new FriendshipFileRepository and loads existing data from the specified file.
     *
     * @param validator the validator used to validate Friendship entities before saving
     * @param fileName  the path to the file where friendships are stored
     */
    public FriendshipFileRepository(Validator<Friendship> validator, String fileName) {
        super(validator, fileName);
        loadData();
    }

    /**
     * Extracts a Friendship entity from a formatted string line.
     *
     * @param line the string line read from the file
     * @return the Friendship object parsed from the given string
     * @throws RepositoryException if the line does not contain exactly 3 fields, if the IDs cannot be parsed as Long, or if the date format is invalid
     */
    @Override
    protected Friendship extractEntity(String line) {
        String[] fields = line.split(";");
        if (fields.length != 3) {
            throw new RepositoryException("Incorrect number of fields for Friendship");
        }
        try {
            Long id1 = Long.parseLong(fields[0]);
            Long id2 = Long.parseLong(fields[1]);
            LocalDateTime date = LocalDateTime.parse(fields[2]);
            return new Friendship(id1, id2, date);
        } catch (NumberFormatException e) {
            throw new RepositoryException("User IDs must be numbers.", e);
        } catch (DateTimeParseException e) {
            throw new RepositoryException("The date format is invalid", e);
        }
    }

    /**
     * Converts a Friendship entity into a formatted string for file storage.
     *
     * @param entity the Friendship entity to be converted
     * @return a string representation of the entity in the format "idUser1;idUser2;date"
     */
    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getIdUser1() + ";" + entity.getIdUser2() + ";" + entity.getDate();
    }
}