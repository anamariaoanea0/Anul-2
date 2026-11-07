import domain.Friendship;
import domain.User;
import factory.RepositoryFactory;
import repository.Repository;
import service.CommunityService;
import service.FriendshipService;
import service.UserService;
import ui.ConsoleUI;
import validator.*;

public class Main {
    public static void main(String[] args) {
        try{
            Validator<User> baseUserValidator = new UserValidator();

            Validator<User> strictNameValidator = user -> {
                if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
                    if (!Character.isUpperCase(user.getFirstName().charAt(0))) {
                        throw new ValidationException("First name must start with an uppercase letter!");
                    }
                }
            };

            Validator<User> userValidator = baseUserValidator.andThen(strictNameValidator);
            Validator<Friendship> friendshipValidator = new FriendshipValidator();

            RepositoryFactory factory = RepositoryFactory.getInstance();
            Repository<Long, User> userRepo = factory.createUserRepository(userValidator);
            Repository<domain.Tuple<Long, Long>, Friendship> friendshipRepo = factory.createFriendshipRepository(friendshipValidator);

            UserService userService = new UserService(userRepo, friendshipRepo);
            FriendshipService friendshipService = new FriendshipService(userRepo, friendshipRepo);
            CommunityService communityService = new CommunityService(userService, friendshipService);

            ConsoleUI ui = new ConsoleUI(userService, friendshipService, communityService);
            ui.run();

        } catch(RepositoryException e){
            System.out.println("Error starting the application: " + e.getMessage());
        }
    }
}