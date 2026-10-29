package ui;

import domain.Friendship;
import domain.User;
import service.SocialNetworkService;
import validator.ValidationException;

import java.util.List;
import java.util.Scanner;

/**
 * Console User Interface for the Social Network application.
 */
public class ConsoleUI {
    private final SocialNetworkService service;
    private final Scanner scanner;

    public ConsoleUI(SocialNetworkService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
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
                    int count = service.getNumberOfCommunities();
                    System.out.println("Total number of communities: " + count);
                    break;
                case "8":
                    handleMostSociableCommunity();
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

            service.addUser(firstName, lastName, age);
            System.out.println("User added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Age must be a valid number!");
        } catch (ValidationException | IllegalArgumentException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    private void handleRemoveUser() {
        try {
            System.out.print("Enter the ID of the user to remove: ");
            Long id = Long.parseLong(scanner.nextLine());

            User deletedUser = service.removeUser(id);
            System.out.println("User " + deletedUser.getFirstName() + " " + deletedUser.getLastName() + " was successfully removed!");

        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a valid number!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error removing user: " + e.getMessage());
        }
    }

    private void handlePrintUsers() {
        System.out.println("\n Registered Users: ");
        for (User user : service.getAllUsers()) {
            System.out.println("ID: " + user.getId() + " , Name: " + user.getFirstName() + " " + user.getLastName() + " , Age: " + user.getAge());
        }
    }
    private void handleAddFriendship() {
        try {
            System.out.print("Enter ID of first user: ");
            Long id1 = Long.parseLong(scanner.nextLine());
            System.out.print("Enter ID of second user: ");
            Long id2 = Long.parseLong(scanner.nextLine());

            service.addFriendship(id1, id2);
            System.out.println("Friendship added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Error: IDs must be valid numbers!");
        } catch (IllegalArgumentException | ValidationException e) {
            System.out.println("Error adding friendship: " + e.getMessage());
        }
    }

    private void handleRemoveFriendship() {
        try{
            System.out.print("Enter ID of first user: ");
            Long id1 = Long.parseLong(scanner.nextLine());
            System.out.print("Enter ID of second user: ");
            Long id2 = Long.parseLong(scanner.nextLine());

            service.removeFriendship(id1, id2);
            System.out.println("Friendship removed successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Error: IDs must be valid numbers!");
        } catch (IllegalArgumentException | ValidationException e) {
            System.out.println("Error adding friendship: " + e.getMessage());
        }
    }

    private void handlePrintFriendships() {
        System.out.println("\n All Friendships:");
        for (Friendship f : service.getAllFriendships()) {
            User u1 = service.getUser(f.getIdUser1());
            User u2 = service.getUser(f.getIdUser2());

            if (u1 != null && u2 != null) {
                System.out.println("Friendship ID " + f.getId() +
                        " : " + u1.getFirstName() + " " + u1.getLastName() + " (ID: " + u1.getId() + ")" +
                        " , " + u2.getFirstName() + " " + u2.getLastName() + " (ID: " + u2.getId() + ")");
            } else {
                System.out.println("Friendship ID: " + f.getId() + " , User1: " + f.getIdUser1() + " , User2: " + f.getIdUser2());
            }
        }
    }

    private void handleMostSociableCommunity() {
        System.out.println("\nMost Sociable Community: ");
        List<Long> communityIds = service.getMostSociableCommunity();

        if (communityIds.isEmpty()) {
            System.out.println("The network is empty.");
            return;
        }

        System.out.println("This community has " + communityIds.size() + " members.");
        System.out.println("Members:");

        for (Long id : communityIds) {
            User user = service.getUser(id);
            if (user != null) {
                System.out.println(" - " + user.getFirstName() + " " + user.getLastName() + " (ID: " + id + ")");
            }
        }
    }
}