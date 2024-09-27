package spotify.server.user;

import java.nio.file.Path;
import java.util.Collection;

public interface UserManagerAPI {

    /**
     * Registers a new user with the given email and password.
     *
     * @param email    the email of the user to be registered
     * @param password the password of the user to be registered
     */
    String registerUser(String email, String password);

    /**
     * Unregisters a user with the given email.
     *
     * @param email the email of the user to be unregistered
     */
    String unregisterUser(String email);

    /**
     * Logs in a user with the given email and password.
     *
     * @param email    the email of the user to be logged in
     * @param password the password of the user to be logged in
     */
    String loginUser(String email, String password);


    /**
     * Logs out a user with the given email.
     *
     * @param email the email of the user to be logged out
     */
    String logoutUser(String email);

    /**
     * Retrieves a collection of User objects.
     *
     * @return          a collection of User objects
     */
    Collection<User> getUsers();

    /**
     * Reads all users from the specified path.
     *
     * @param  path  the path to read users from
     */
    void readAllUsers(Path path);

    /**
     * Check if the user with the given email is logged in.
     *
     * @param  email  the email of the user to check
     * @return       true if the user is logged in, false otherwise
     */
    boolean isUserLoggedIn(String email);

    /**
     * Check if the user is registered.
     *
     * @param  arg  the user identifier
     * @return      true if the user is registered, false otherwise
     */
    boolean isUserRegistered(String arg);
}
