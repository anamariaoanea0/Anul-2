package repository;

import domain.Friendship;
import validator.Validator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * File repository for Friendship entities.
 * Extends the InMemoryRepository to reuse the HashMap and Validator.
 */
public class FriendshipFileRepository extends InMemoryRepository<Long, Friendship> {
    private String fileName;

    public FriendshipFileRepository(Validator<Friendship> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    /**
     * Loads the data from the CSV file into the memory structure.
     */
    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 3) {
                    Long id = Long.parseLong(parts[0]);
                    Long idUser1 = Long.parseLong(parts[1]);
                    Long idUser2 = Long.parseLong(parts[2]);

                    Friendship friendship = new Friendship(idUser1, idUser2);
                    friendship.setId(id);

                    super.save(friendship);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    /**
     * Rewrites the entire file with the current data from the memory.
     */
    private void writeToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Friendship friendship : entities.values()) {
                String line = friendship.getId() + "," + friendship.getIdUser1() + "," + friendship.getIdUser2();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Override the 'save' function to also store the newly entered data in the file.
     *
     * @param entity friendship entity to save, entity must be not null
     * @return the new friendship saved
     */
    @Override
    public Friendship save(Friendship entity) {
        Friendship savedFriendship = super.save(entity);
        if (savedFriendship == null) {
            writeToFile();
        }
        return savedFriendship;
    }

    /**
     * Override the 'delete' function to also save the changes to the file.
     *
     * @param id friendship id to be deleted, id must be not null
     * @return the deleted friendship
     */
    @Override
    public Friendship delete(Long id) {
        Friendship deletedFriendship = super.delete(id);
        if (deletedFriendship != null) {
            writeToFile();
        }
        return deletedFriendship;
    }
}