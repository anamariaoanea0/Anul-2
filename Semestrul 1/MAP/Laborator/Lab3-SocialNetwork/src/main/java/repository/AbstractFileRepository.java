package repository;

import domain.Entity;
import validator.RepositoryException;
import validator.Validator;

import java.io.*;

/**
 * Repository that keeps the entities in memory and in a text file.
 */
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E>{
    private final String fileName;

    public AbstractFileRepository(Validator<E> validator, String fileName){
        super(validator);
        this.fileName = fileName;
    }

    /**
     * Creates an entity from a line of the file.
     */
    protected abstract E extractEntity(String line);

    /**
     * Converts an entity into a line of the file.
     */
    protected abstract String createEntityAsString(E entity);

    /**
     * Reads all lines of the file and saves the entities in memory.
     * Corrupted lines are reported and ignored.
     */
    protected void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;

                try {
                    E entity = extractEntity(line);
                    super.save(entity);
                } catch (Exception e) {
                    System.out.println("The line " + lineNumber + " from " + fileName + " is invalid: " + e.getMessage() + ". The line was ignored.");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("The file " + fileName + " doesn't exist. Empty repository.");
        } catch (IOException e) {
            throw new RepositoryException("Error while reading the file " + fileName, e);
        }
    }

    /**
     * Rewrites the whole file with the entities currently in memory.
     *
     * @throws RuntimeException if the file cannot be written
     */
    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (E entity : findAll()) {
                writer.write(createEntityAsString(entity));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RepositoryException("Error while writing in the file " + fileName, e);
        }
    }

    /**
     * Override the 'save' function to also store the newly entered data in the file.
     *
     * @param entity entity to save, entity must be not null
     * @return the new entity saved
     */
    @Override
    public E save(E entity) {
        E result = super.save(entity);
        if (result == null) {
            writeToFile();
        }
        return result;
    }

    /**
     * Override the 'delete' function to also save the changes to the file.
     *
     * @param id entity id to be deleted, id must be not null
     * @return the deleted entity
     */
    @Override
    public E delete(ID id) {
        E result = super.delete(id);
        if (result != null) {
            writeToFile();
        }
        return result;
    }
}
