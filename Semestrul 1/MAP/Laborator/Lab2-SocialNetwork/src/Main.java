import domain.Friendship;
import domain.User;
import repository.FriendshipFileRepository;
import repository.Repository;
import repository.UserFileRepository;
import service.SocialNetworkService;
import ui.ConsoleUI;
import validator.FriendshipValidator;
import validator.UserValidator;
import validator.Validator;

public class Main {
    public static void main(String[] args) {
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();

        Repository<Long, User> userRepo = new UserFileRepository(userValidator, "users.csv");
        Repository<Long, Friendship> friendshipRepo = new FriendshipFileRepository(friendshipValidator, "friendship.csv");

        SocialNetworkService service = new SocialNetworkService(userRepo, friendshipRepo);

        ConsoleUI ui = new ConsoleUI(service);
        ui.run();
    }
}