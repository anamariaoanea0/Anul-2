package service;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import repository.Repository;
import validator.ServiceException;

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
        if (userRepo.findOne(id1) == null || userRepo.findOne(id2) == null) {
            throw new ServiceException("At least one of the users does not exist!");
        }
        if (id1.equals(id2)) {
            throw new ServiceException("A user cannot become friends with themselves!");
        }

        Friendship newFriendship = new Friendship(id1, id2);
        if (friendshipRepo.save(newFriendship) != null) {
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
        if (friendshipRepo.delete(id) == null) {
            throw new ServiceException("Friendship does not exist");
        }
    }

    /**
     * Retrieves all friendships from the network.
     *
     * @return an Iterable containing all Friendship entities
     */
    public Iterable<Friendship> getAllFriendships() {
        return friendshipRepo.findAll();
    }

}
