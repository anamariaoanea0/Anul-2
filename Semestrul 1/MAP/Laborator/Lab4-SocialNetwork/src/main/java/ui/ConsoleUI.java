package ui;

import domain.Friendship;
import domain.User;
import service.CommunityService;
import service.FriendshipService;
import service.UserService;
import util.paging.Page;
import validator.RepositoryException;
import validator.ServiceException;
import validator.ValidationException;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Console User Interface for the Social Network application.
 */
public class ConsoleUI {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final CommunityService communityService;
    private final Scanner scanner;
    private final int pageSize;

    public ConsoleUI(UserService userService, FriendshipService friendshipService, CommunityService communityService, int pageSize) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.communityService = communityService;
        this.scanner = new Scanner(System.in);
        this.pageSize = pageSize;
    }

    private void printMenu() {
        System.out.println("\n Social Network Menu");
        System.out.println("1. Add a new user");
        System.out.println("2. Remove a user");
        System.out.println("3. Print all users");
        System.out.println("4. Add a friendship");
        System.out.println("5. Remove a friendship.");
        System.out.println("6. Print all friendships");
        System.out.println("7. Show number of communities");
        System.out.println("8. Show most sociable community (longest path)");
        System.out.println("9. Print users (paginated)");
        System.out.println("10. Print friends of a user (paginated)");
        System.out.println("E. Exit");
        System.out.print("Choose an option: ");
    }

    public void run() {
        while (true) {
            printMenu();
            String option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    handleAddUser();
                    break;
                case "2":
                    handleRemoveUser();
                    break;
                case "3":
                    handlePrintUsers();
                    break;
                case "4":
                    handleAddFriendship();
                    break;
                case "5":
                    handleRemoveFriendship();
                    break;
                case "6":
                    handlePrintFriendships();
                    break;
                case "7":
                    int count = communityService.getNumberOfCommunities();
                    System.out.println("Total number of communities: " + count);
                    break;
                case "8":
                    handleMostSociableCommunity();
                    break;
                case "9":
                    handlePrintUsersPaginated();
                    break;
                case "10":
                    handlePrintFriendsPaginated();
                    break;
                case "E":
                    System.out.println("Exiting application.");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private void handleAddUser() {
        try {
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter age: ");
            int age = Integer.parseInt(scanner.nextLine());

            userService.addUser(firstName, lastName, age);
            System.out.println("User added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Age must be a valid number!");
        } catch (ValidationException | ServiceException | RepositoryException | IllegalArgumentException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    private void handleRemoveUser() {
        try {
            System.out.print("Enter the ID of the user to remove: ");
            Long id = Long.parseLong(scanner.nextLine());

            User deletedUser = userService.removeUser(id);
            System.out.println("User " + deletedUser.getFirstName() + " " + deletedUser.getLastName() + " was successfully removed!");

        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a valid number!");
        } catch (ValidationException | ServiceException | RepositoryException | IllegalArgumentException e) {
            System.out.println("Error removing user: " + e.getMessage());
        }
    }

    private void handlePrintUsers() {
        System.out.println("\n Registered Users: ");
        userService.getAllUsers().forEach(user ->
                System.out.println("ID: " + user.getId() + " , Name: " + user.getFirstName() + " " + user.getLastName() + " , Age: " + user.getAge())
        );
    }

    private void handleAddFriendship() {
        try {
            System.out.print("Enter ID of first user: ");
            Long id1 = Long.parseLong(scanner.nextLine());
            System.out.print("Enter ID of second user: ");
            Long id2 = Long.parseLong(scanner.nextLine());

            friendshipService.addFriendship(id1, id2);
            System.out.println("Friendship added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Error: IDs must be valid numbers!");
        } catch (ValidationException | ServiceException | RepositoryException | IllegalArgumentException e) {
            System.out.println("Error adding friendship: " + e.getMessage());
        }
    }

    private void handleRemoveFriendship() {
        try{
            System.out.print("Enter ID of first user: ");
            Long id1 = Long.parseLong(scanner.nextLine());
            System.out.print("Enter ID of second user: ");
            Long id2 = Long.parseLong(scanner.nextLine());

            friendshipService.removeFriendship(id1, id2);
            System.out.println("Friendship removed successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Error: IDs must be valid numbers!");
        } catch (ValidationException | ServiceException | RepositoryException | IllegalArgumentException e) {
            System.out.println("Error removing friendship: " + e.getMessage());
        }
    }

    private void handlePrintFriendships() {
        System.out.println("\n All Friendships:");
        friendshipService.getAllFriendships().forEach(f -> {
            try {
                User u1 = userService.getUser(f.getIdUser1());
                User u2 = userService.getUser(f.getIdUser2());
                System.out.println("Friendship: " + u1.getFirstName() + " " + u1.getLastName() + " (ID: " + u1.getId() + ")" +
                        " , " + u2.getFirstName() + " " + u2.getLastName() + " (ID: " + u2.getId() + ")" + ", Date: " + f.getDate());
            } catch (ServiceException e) {
                System.out.println("Friendship: " + " , User1: " + f.getIdUser1() + " , User2: " + f.getIdUser2() + "Date: " + f.getDate());
            }
        });
    }

    private void handleMostSociableCommunity() {
        System.out.println("\nMost Sociable Community: ");
        List<Long> communityIds = communityService.getMostSociableCommunity();

        if (communityIds.isEmpty()) {
            System.out.println("The network is empty.");
            return;
        }

        System.out.println("This community has " + communityIds.size() + " members.");
        System.out.println("Members:");

        communityIds.stream()
                .map(id -> {
                    try {
                        return userService.getUser(id);
                    } catch (ServiceException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .forEach(user ->
                        System.out.println(" - " + user.getFirstName() + " " + user.getLastName() + " (ID: " + user.getId() + ")")
                );
    }


    private void handlePrintUsersPaginated() {
        int pageNumber = 0;
        while (true) {
            try {
                Page<User> page = userService.getUsersOnPage(pageNumber, pageSize);
                int totalElements = page.getTotalNumberOfElements();
                int totalPages = Math.max(1, (totalElements + pageSize - 1) / pageSize);

                if (pageNumber >= totalPages) {
                    pageNumber = totalPages - 1;
                    page = userService.getUsersOnPage(pageNumber, pageSize);
                }

                System.out.println("\n Users | Page " + (pageNumber + 1) + " of " + totalPages + " (Total: " + totalElements + ") ");
                if (totalElements == 0) {
                    System.out.println("No users found.");
                } else {
                    page.getElementsOnPage().forEach(user ->
                            System.out.println("ID: " + user.getId() + ", Name: " + user.getFirstName() + " " + user.getLastName() + ", Age: " + user.getAge())
                    );
                }

                System.out.print("[n]ext, [p]revious, [q]uit: ");
                String cmd = scanner.nextLine().trim().toLowerCase();
                switch (cmd) {
                    case "n" -> {
                        if (pageNumber < totalPages - 1) pageNumber++;
                        else System.out.println("Already on the last page!");
                    }
                    case "p" -> {
                        if (pageNumber > 0) pageNumber--;
                        else System.out.println("Already on the first page!");
                    }
                    case "q" -> { return; }
                    default -> System.out.println("Unknown command!");
                }
            } catch (IllegalArgumentException | ServiceException | RepositoryException e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }
        }
    }

    private void handlePrintFriendsPaginated() {
        System.out.print("Enter user ID: ");
        Long userId;
        try {
            userId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a valid number!");
            return;
        }

        int pageNumber = 0;
        while (true) {
            try {
                Page<User> page = friendshipService.getFriendsOnPage(userId, pageNumber, pageSize);
                int totalElements = page.getTotalNumberOfElements();
                int totalPages = Math.max(1, (totalElements + pageSize - 1) / pageSize);

                if (pageNumber >= totalPages) {
                    pageNumber = totalPages - 1;
                    page = friendshipService.getFriendsOnPage(userId, pageNumber, pageSize);
                }

                System.out.println("\n Friends of user " + userId + " | Page " + (pageNumber + 1) + " of " + totalPages + " (Total: " + totalElements + ") ");
                if (totalElements == 0) {
                    System.out.println("This user has no friends.");
                } else {
                    page.getElementsOnPage().forEach(friend ->
                            System.out.println("ID: " + friend.getId() + ", Name: " + friend.getFirstName() + " " + friend.getLastName() + ", Age: " + friend.getAge())
                    );
                }

                System.out.print("[n]ext, [p]revious, [q]uit: ");
                String cmd = scanner.nextLine().trim().toLowerCase();
                switch (cmd) {
                    case "n" -> {
                        if (pageNumber < totalPages - 1) pageNumber++;
                        else System.out.println("Already on the last page!");
                    }
                    case "p" -> {
                        if (pageNumber > 0) pageNumber--;
                        else System.out.println("Already on the first page!");
                    }
                    case "q" -> { return; }
                    default -> System.out.println("Unknown command!");
                }
            } catch (IllegalArgumentException | ServiceException | RepositoryException e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }
        }
    }
}