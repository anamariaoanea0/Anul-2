package service;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import repository.Repository;
import validator.ServiceException;
import validator.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service class for managing the social network logic for User entity.
 */
public class UserService {
    private final Repository<Long, User> userRepo;
    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepo;

    public UserService(Repository<Long, User> userRepo, Repository<Tuple<Long, Long>, Friendship> friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
    }

    /**
     * Adds a new user to the network.
     * Generates a new ID automatically based on the existing max ID.
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param age       the age of the user
     * @throws ValidationException if the user data is invalid
     */
    public void addUser(String firstName, String lastName, int age) {
        User newUser = new User(null, firstName, lastName, age);
        Optional<User> result = userRepo.save(newUser);
        if (result.isPresent()) {
            throw new ServiceException("A user with this ID already exists!");
        }
    }

    /**
     * Removes a user from the network by ID.
     * Also deletes all friendships associated with this user.
     *
     * @param id the ID of the user to be removed
     * @return the deleted User entity
     * @throws ServiceException if the user does not exist
     */
    public User removeUser(Long id) {
        User userToDelete = userRepo.findOne(id)
                .orElseThrow(() -> new ServiceException("User with the given ID does not exist!"));

        List<Tuple<Long, Long>> friendshipsToDelete = StreamSupport.stream(friendshipRepo.findAll().spliterator(), false)
                .filter(f -> f.getIdUser1().equals(id) || f.getIdUser2().equals(id))
                .map(Friendship::getId)
                .collect(Collectors.toList());

        friendshipsToDelete.forEach(friendshipRepo::delete);

        return userRepo.delete(id).orElse(userToDelete);
    }
    /**
     * Retrieves all users from the network.
     *
     * @return an Iterable containing all User entities
     */
    public Iterable<User> getAllUsers() {
        return userRepo.findAll();
    }

    /**
     * Returns a user based on the ID.
     */
    public User getUser(Long id) {
        return userRepo.findOne(id)
                .orElseThrow(() -> new ServiceException("User with ID " + id + " not found!"));
    }
}
