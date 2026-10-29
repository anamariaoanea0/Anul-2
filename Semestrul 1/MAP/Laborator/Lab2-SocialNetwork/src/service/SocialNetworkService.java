package service;

import domain.Friendship;
import domain.User;
import repository.Repository;
import validator.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing the social network logic.
 */
public class SocialNetworkService {

    private final Repository<Long, User> userRepo;
    private final Repository<Long, Friendship> friendshipRepo;

    public SocialNetworkService(Repository<Long, User> userRepo, Repository<Long, Friendship> friendshipRepo) {
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
        Long newId = getNextUserId();
        User newUser = new User(newId, firstName, lastName, age);

        User result = userRepo.save(newUser);
        if (result != null) {
            throw new IllegalArgumentException("A user with this ID already exists!");
        }
    }

    /**
     * Removes a user from the network by ID.
     * Also deletes all friendships associated with this user.
     *
     * @param id the ID of the user to be removed
     * @return the deleted User entity
     * @throws IllegalArgumentException if the user does not exist
     */
    public User removeUser(Long id) {
        User userToDelete = userRepo.findOne(id);
        if (userToDelete == null) {
            throw new IllegalArgumentException("User with the given ID does not exist!");
        }

        List<Long> friendshipsToDelete = new ArrayList<>();
        for (Friendship friendship : friendshipRepo.findAll()) {
            if (friendship.getIdUser1().equals(id) || friendship.getIdUser2().equals(id)) {
                friendshipsToDelete.add(friendship.getId());
            }
        }

        for (Long friendshipId : friendshipsToDelete) {
            friendshipRepo.delete(friendshipId);
        }

        return userRepo.delete(id);
    }

    /**
     * Helper method to generate an auto-incremented ID for new users.
     *
     * @return the next available Long ID
     */
    private Long getNextUserId() {
        Long maxId = 0L;
        for (User user : userRepo.findAll()) {
            if (user.getId() > maxId) {
                maxId = user.getId();
            }
        }
        return maxId + 1;
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
        return userRepo.findOne(id);
    }

    /**
     * Adds a new friendship to the network.
     * Generates a new ID automatically based on the existing max ID.
     *
     * @param id1 the ID of the first friend
     * @param id2 the ID of the second friend
     * @throws IllegalArgumentException if the users data is invalid or the friendship already exists.
     */
    public void addFriendship(Long id1, Long id2) {
        if (userRepo.findOne(id1) == null || userRepo.findOne(id2) == null) {
            throw new IllegalArgumentException("At least one of the users does not exist!");
        }

        for (Friendship f : friendshipRepo.findAll()) {
            if ((f.getIdUser1().equals(id1) && f.getIdUser2().equals(id2)) ||
                    (f.getIdUser1().equals(id2) && f.getIdUser2().equals(id1))) {
                throw new IllegalArgumentException("This friendship already exists!");
            }
        }

        Long newId = getNextFriendshipId();
        Friendship newFriendship = new Friendship(id1, id2);
        newFriendship.setId(newId);
        friendshipRepo.save(newFriendship);
    }

    /**
     * Remove a friendship from the network by users IDs.
     * @param id1 the ID of the first user.
     * @param id2 the id of the second user.
     * @throws IllegalArgumentException if the friendship does not exist.
     */
    public void removeFriendship(Long id1, Long id2) {
        Long friendshipIdToDelete = null;

        for (Friendship f : friendshipRepo.findAll()) {
            if ((f.getIdUser1().equals(id1) && f.getIdUser2().equals(id2)) ||
                    (f.getIdUser1().equals(id2) && f.getIdUser2().equals(id1))) {
                friendshipIdToDelete = f.getId();
                break;
            }
        }

        if (friendshipIdToDelete == null) {
            throw new IllegalArgumentException("Friendship does not exist!");
        }

        friendshipRepo.delete(friendshipIdToDelete);
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
     * Helper method to generate an auto-incremented ID for new friendships.
     *
     * @return the next available Long ID
     */
    private Long getNextFriendshipId() {
        Long maxId = 0L;
        for (Friendship f : friendshipRepo.findAll()) {
            if (f.getId() > maxId) {
                maxId = f.getId();
            }
        }
        return maxId + 1;
    }

    /**
     * Build the adjacency list for the network graph.
     *
     * @return an adjacency list containing all users
     */
    private Map<Long, List<Long>> buildGraph() {
        Map<Long, List<Long>> adjList = new HashMap<>();

        for (User u : userRepo.findAll()) {
            adjList.put(u.getId(), new ArrayList<>());
        }

        for (Friendship f : friendshipRepo.findAll()) {
            adjList.get(f.getIdUser1()).add(f.getIdUser2());
            adjList.get(f.getIdUser2()).add(f.getIdUser1());
        }
        return adjList;
    }

    /**
     * Classic DFS traversal to find all members of a community.
     */
    private void dfs(Long nodeId, Map<Long, List<Long>> adjList, List<Long> visited) {
        visited.add(nodeId);
        for (Long neighbor : adjList.get(nodeId)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, adjList, visited);
            }
        }
    }

    /**
     * Calculate the number of communities (connected components).
     *
     * @return the number of communities.
     */
    public int getNumberOfCommunities() {
        Map<Long, List<Long>> adjList = buildGraph();
        List<Long> visited = new ArrayList<>();
        int communitiesCount = 0;

        for (User u : userRepo.findAll()) {
            if (!visited.contains(u.getId())) {
                communitiesCount++;
                dfs(u.getId(), adjList, visited);
            }
        }
        return communitiesCount;
    }

    /**
     * Find all communities (connected components) in the network.
     *
     * @return a list of all communities.
     */
    private List<List<Long>> getAllCommunities() {
        Map<Long, List<Long>> adjList = buildGraph();
        List<Long> visited = new ArrayList<>();
        List<List<Long>> communities = new ArrayList<>();

        for (User u : userRepo.findAll()) {
            if (!visited.contains(u.getId())) {
                List<Long> currentCommunity = new ArrayList<>();
                dfsForCommunity(u.getId(), adjList, visited, currentCommunity);
                communities.add(currentCommunity);
            }
        }
        return communities;
    }

    /**
     * DFS adapted to save the nodes that are part of the current community.
     */
    private void dfsForCommunity(Long nodeId, Map<Long, List<Long>> adjList, List<Long> visited, List<Long> currentCommunity) {
        visited.add(nodeId);
        currentCommunity.add(nodeId);
        for (Long neighbor : adjList.get(nodeId)) {
            if (!visited.contains(neighbor)) {
                dfsForCommunity(neighbor, adjList, visited, currentCommunity);
            }
        }
    }

    /**
     * @return User IDs from the community with the longest path.
     */
    public List<Long> getMostSociableCommunity() {
        Map<Long, List<Long>> adjList = buildGraph();
        List<List<Long>> communities = getAllCommunities();

        List<Long> mostSociableCommunity = new ArrayList<>();
        int maxPathLength = -1;

        for (List<Long> community : communities) {
            int currentCommunityMaxPath = 0;

            for (Long node : community) {
                int pathLen = longestPathFromNode(node, adjList, new ArrayList<>());
                if (pathLen > currentCommunityMaxPath) {
                    currentCommunityMaxPath = pathLen;
                }
            }

            if (currentCommunityMaxPath > maxPathLength) {
                maxPathLength = currentCommunityMaxPath;
                mostSociableCommunity = community;
            }
        }

        return mostSociableCommunity;
    }

    /**
     * Backtracking to find the longest simple path starting from a given node.
     *
     * @param node the node from which the search begins
     * @param adjList adjacency list containing all users
     * @param pathVisited the list of visited nodes
     * @return the longest simple path starting from a given node.
     */
    private int longestPathFromNode(Long node, Map<Long, List<Long>> adjList, List<Long> pathVisited) {
        int maxLength = 0;
        pathVisited.add(node);

        for (Long neighbor : adjList.get(node)) {
            if (!pathVisited.contains(neighbor)) {
                int len = longestPathFromNode(neighbor, adjList, pathVisited);
                if (len > maxLength) {
                    maxLength = len;
                }
            }
        }

        pathVisited.remove(node);
        return maxLength + 1;
    }
}