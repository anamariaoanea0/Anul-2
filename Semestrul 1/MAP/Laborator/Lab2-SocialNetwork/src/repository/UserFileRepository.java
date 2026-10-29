package repository;

import domain.User;
import validator.Validator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * File repository for User entities.
 * Extends the InMemoryRepository to reuse the HashMap and Validator.
 */
public class UserFileRepository extends InMemoryRepository<Long, User> {
    private String fileName;

    public UserFileRepository(Validator<User> validator, String fileName) {
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
                if (parts.length == 4) {
                    Long id = Long.parseLong(parts[0]);
                    String firstName = parts[1];
                    String lastName = parts[2];
                    int age = Integer.parseInt(parts[3]);

                    User user = new User(id, firstName, lastName, age);
                    super.save(user);
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
            for (User user : entities.values()) {
                String line = user.getId() + "," + user.getFirstName() + "," + user.getLastName() + "," + user.getAge();
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
     * @param entity user entity to save, entity must be not null
     * @return the new user saved
     */
    @Override
    public User save(User entity) {
        User savedUser = super.save(entity);
        if (savedUser == null) {
            writeToFile();
        }
        return savedUser;
    }

    /**
     * Override the 'delete' function to also save the changes to the file.
     *
     * @param id user id to be deleted, id must be not null
     * @return the deleted user
     */
    @Override
    public User delete(Long id) {
        User deletedUser = super.delete(id);
        if (deletedUser != null) {
            writeToFile();
        }
        return deletedUser;
    }
}