package repository;

import domain.Entity;
import validator.RepositoryException;
import validator.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            System.out.println("The file " + fileName + " doesn't exist. Empty repository.");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(path);

            IntStream.range(0, lines.size()).forEach(index -> {
                String line = lines.get(index);
                if (!line.isBlank()) {
                    try {
                        E entity = extractEntity(line);
                        super.save(entity);
                    } catch (Exception e) {
                        System.out.println("The line " + (index + 1) + " from " + fileName + " is invalid: " + e.getMessage() + ". The line was ignored.");
                    }
                }
            });
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
        Path path = Paths.get(fileName);
        try {
            List<String> lines = StreamSupport.stream(findAll().spliterator(), false)
                    .map(this::createEntityAsString)
                    .collect(Collectors.toList());

            Files.write(path, lines);
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
    public Optional<E> save(E entity) {
        Optional<E> optional = super.save(entity);
        if (optional.isEmpty()) {
            writeToFile();
        }
        return optional;
    }

    /**
     * Override the 'delete' function to also save the changes to the file.
     *
     * @param id entity id to be deleted, id must be not null
     * @return the deleted entity
     */
    @Override
    public Optional<E> delete(ID id) {
        Optional<E> optional = super.delete(id);
        if (optional.isPresent()) {
            writeToFile();
        }
        return optional;
    }
}
