package gist.mockito;

public class UserService {
    private final UserManager userManager;

    public UserService() {
        this(new UserManager());
    }

    public UserService(UserManager userManager) {
        this.userManager = userManager;
    }

    public void save(String name) {
        userManager.save(name);
    }
}
