package service;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<Long, List<Long>> adjList = new HashMap<>();

        for (User u : userService.getAllUsers()) {
            adjList.put(u.getId(), new ArrayList<>());
        }

        for (Friendship f : friendshipService.getAllFriendships()) {
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

        for (User u : userService.getAllUsers()) {
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

        for (User u : userService.getAllUsers()) {
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
