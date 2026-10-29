import domain.Friendship;
import domain.User;
import factory.RepositoryFactory;
import repository.Repository;
import service.CommunityService;
import service.FriendshipService;
import service.UserService;
import ui.ConsoleUI;
import validator.FriendshipValidator;
import validator.UserValidator;
import validator.Validator;

public class Main {
    public static void main(String[] args) {
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();

        RepositoryFactory factory = RepositoryFactory.getInstance();
        Repository<Long, User> userRepo = factory.createUserRepository(userValidator);
        Repository<domain.Tuple<Long, Long>, Friendship> friendshipRepo = factory.createFriendshipRepository(friendshipValidator);

        UserService userService = new UserService(userRepo, friendshipRepo);
        FriendshipService friendshipService = new FriendshipService(userRepo, friendshipRepo);
        CommunityService communityService = new CommunityService(userService, friendshipService);

        ConsoleUI ui = new ConsoleUI(userService, friendshipService, communityService);
        ui.run();
    }
}