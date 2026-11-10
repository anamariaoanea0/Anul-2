package service;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import repository.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service class for managing the social network logic.
 */
public class CommunityService {
    private final UserService userService;
    private final FriendshipService friendshipService;

    public CommunityService(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    /**
     * Build the adjacency list for the network graph.
     *
     * @return an adjacency list containing all users
     */
    private Map<Long, List<Long>> buildGraph() {
        Map<Long, List<Long>> adjList = StreamSupport.stream(userService.getAllUsers().spliterator(), false)
                .collect(Collectors.toMap(User::getId, u -> new ArrayList<>()));

        StreamSupport.stream(friendshipService.getAllFriendships().spliterator(), false)
                .forEach(f -> {
                    adjList.get(f.getIdUser1()).add(f.getIdUser2());
                    adjList.get(f.getIdUser2()).add(f.getIdUser1());
                });

        return adjList;
    }

    /**
     * Classic DFS traversal to find all members of a community.
     */
    private void dfs(Long nodeId, Map<Long, List<Long>> adjList, List<Long> visited, List<Long> currentCommunity) {
        visited.add(nodeId);
        if (currentCommunity != null) {
            currentCommunity.add(nodeId);
        }

        adjList.get(nodeId).stream()
                .filter(neighbor -> !visited.contains(neighbor))
                .forEach(neighbor -> dfs(neighbor, adjList, visited, currentCommunity));
    }

    /**
     * Find all communities (connected components) in the network.
     *
     * @return a list of all communities.
     */
    private List<List<Long>> getAllCommunities() {
        Map<Long, List<Long>> adjList = buildGraph();
        List<Long> visited = new ArrayList<>();

        return StreamSupport.stream(userService.getAllUsers().spliterator(), false)
                .map(User::getId)
                .filter(id -> !visited.contains(id))
                .map(id -> {
                    List<Long> currentCommunity = new ArrayList<>();
                    dfs(id, adjList, visited, currentCommunity);
                    return currentCommunity;
                })
                .collect(Collectors.toList());
    }

    /**
     * Calculate the number of communities (connected components).
     *
     * @return the number of communities.
     */
    public int getNumberOfCommunities() {
        return getAllCommunities().size();
    }

    /**
     * DFS adapted to save the nodes that are part of the current community.
     *
     * @param nodeId the ID of the current node (user) being explored
     * @param adjList adjacency list containing all users
     * @param visited the list of node IDs that have already been visited
     * @param currentCommunity the list accumulating the node IDs that belong to the current community
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

        return getAllCommunities().stream()
                .max(Comparator.comparingInt(community -> community.stream()
                        .mapToInt(node -> longestPathFromNode(node, adjList, new ArrayList<>()))
                        .max()
                        .orElse(0)))
                .orElse(Collections.emptyList());
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
