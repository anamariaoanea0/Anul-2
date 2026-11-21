package service;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import repository.FriendshipDbRepository;
import repository.Repository;
import util.paging.Page;
import util.paging.Pageable;
import validator.ServiceException;

import java.util.Optional;

/**
 * Service class for managing the social network logic for Friendship entity.
 */
public class FriendshipService {
    private final Repository<Long, User> userRepo;
    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepo;

    public FriendshipService(Repository<Long, User> userRepo, Repository<Tuple<Long, Long>, Friendship> friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
    }

    /**
     * Adds a new friendship to the network.
     *
     * @param id1 the ID of the first friend
     * @param id2 the ID of the second friend
     * @throws ServiceException if the users data is invalid or the friendship already exists.
     */
    public void addFriendship(Long id1, Long id2) {
        userRepo.findOne(id1).orElseThrow(() -> new ServiceException("First user does not exist!"));
        userRepo.findOne(id2).orElseThrow(() -> new ServiceException("Second user does not exist!"));

        if (id1.equals(id2)) {
            throw new ServiceException("A user cannot become friends with themselves!");
        }

        Friendship newFriendship = new Friendship(id1, id2);
        Optional<Friendship> result = friendshipRepo.save(newFriendship);
        if (result.isPresent()) {
            throw new ServiceException("This friendship already exists!");
        }
    }

    /**
     * Remove a friendship from the network by users IDs.
     *
     * @param id1 the ID of the first user.
     * @param id2 the id of the second user.
     * @throws ServiceException if the friendship does not exist.
     */
    public void removeFriendship(Long id1, Long id2) {
        Tuple<Long, Long> id = new Tuple<>(Math.min(id1, id2), Math.max(id1, id2));
        friendshipRepo.delete(id).orElseThrow(() -> new ServiceException("Friendship does not exist"));
    }

    /**
     * Retrieves all friendships from the network.
     *
     * @return an Iterable containing all Friendship entities
     */
    public Iterable<Friendship> getAllFriendships() {
        return friendshipRepo.findAll();
    }

    /**
     * Retrieves a page of friends for a specific user.
     *
     * @param userId the ID of the user whose friends are requested
     * @param pageNumber the zero-based page index, must be >= 0
     * @param pageSize the number of friends per page, must be > 0
     * @return a Page containing the friends on the requested page and the total friend count
     * @throws IllegalArgumentException if pageNumber < 0 or pageSize <= 0
     * @throws ServiceException if the user does not exist or the configured repository does not support pagination
     */
    public Page<User> getFriendsOnPage(Long userId, int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page number must be >= 0 and page size must be > 0!");
        }
        userRepo.findOne(userId)
                .orElseThrow(() -> new ServiceException("User with ID " + userId + " does not exist!"));

        if (friendshipRepo instanceof FriendshipDbRepository dbRepo) {
            return dbRepo.findFriendsOnPage(userId, new Pageable(pageNumber, pageSize));
        }
        throw new ServiceException("Pagination is only supported for DATABASE repository!");
    }

}
