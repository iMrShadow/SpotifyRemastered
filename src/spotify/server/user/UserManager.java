package spotify.server.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public class UserManager implements UserManagerAPI {

    public static final String USER_DATA_FILE_PATH = "users.csv";
    public static final String TEST_USER_DATA_FILE_PATH = "testUsers.csv";

    private final String usersFilePath;
    private final Map<String, User> users;
    private final Map<String, Boolean> usersLoggedIn;
    private final Object usersLock = new Object();

    public UserManager(Map<String, User> users, Map<String, Boolean> usersLoggedIn, String useDataFilePath) {
        this.users = users;
        this.usersLoggedIn = usersLoggedIn;
        this.usersFilePath = useDataFilePath;
        initializeUserDataFile(useDataFilePath);
        readAllUsers(Path.of(useDataFilePath));
    }

    private void initializeUserDataFile(String path) {
        Path userFilePath = Path.of(path);
        if (!Files.exists(userFilePath)) {
            try {
                Files.createFile(userFilePath);
                Files.writeString(userFilePath, "email,password" + System.lineSeparator(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize user data file.", e);
            }
        }
    }

    public String registerUser(String email, String password) {
        if (isUserRegistered(email)) {
            return "User is already registered.";
        }

        User newUser = new User(email, password);
        users.put(email, newUser);
        saveUser(newUser);

        return "User registered successfully.";
    }

    @Override
    public String unregisterUser(String email) {
        if (!usersLoggedIn.containsKey(email)) {
            return "User not logged in.";
        }
        if (!users.containsKey(email)) {
            return "User not found.";
        }
        logoutUser(email);
        removeUser(email);

        return "User unregistered successfully.";
    }

    @Override
    public String loginUser(String email, String password) {
        if (!users.containsKey(email)) {
            return "User not registered.";
        }
        User user = users.get(email);
        if (isUserLoggedIn(email)) {
            System.out.println("User already logged in.");
            return "User already logged in.";
        }

        if (user.password().equals(password)) {
            usersLoggedIn.put(email, true);
            return "Successfully logged in! Welcome, " + getUsername(email) + "!";
        } else {
            return "Incorrect password.";
        }
    }

    @Override
    public String logoutUser(String email) {
        if (!isUserLoggedIn(email)) {
            return "User not logged in.";
        }

        usersLoggedIn.remove(email);
        return "User logged out successfully.";
    }

    public List<User> getUsers() {
        return users.values().stream().toList();
    }

    @Override
    public void readAllUsers(Path path) {
        synchronized (usersLock) {
            try (BufferedReader fileReader = Files.newBufferedReader(path)) {
                fileReader.lines()
                        .skip(1)
                        .map(line -> line.split(","))
                        .forEach(userData -> users.put(userData[0], new User(userData[0], userData[1])));
            } catch (IOException e) {
                throw new RuntimeException("Failed to read user data file.", e);
            }
        }
    }

    private void saveUser(User user) {
        synchronized (usersLock) {
            try {
                Path userFilePath = Path.of(usersFilePath);
                boolean fileExists = Files.exists(userFilePath);

                if (!fileExists) {
                    throw new IOException("File does not exist.");
                }

                String userData = user.email() + "," + user.password() + System.lineSeparator();
                Files.writeString(userFilePath, userData, StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save user data.", e);
            }
        }
    }

    private void removeUser(String email) {
        synchronized (usersLock) {
            try {
                if (users.containsKey(email)) {
                    users.remove(email);

                    List<User> allUsers = getUsers();
                    Path userFilePath = Path.of(usersFilePath);

                    Files.writeString(userFilePath, "");

                    for (User user : allUsers) {
                        String userData = user.email() + "," + user.password() + System.lineSeparator();
                        Files.writeString(userFilePath, userData, StandardOpenOption.APPEND);
                    }
                } else {
                    System.out.println("User not found.");
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to save user data.", e);
            }
        }
    }

    @Override
    public boolean isUserLoggedIn(String email) {
        return usersLoggedIn.getOrDefault(email, false);
    }

    @Override
    public boolean isUserRegistered(String email) {
        return users.containsKey(email);
    }

    private String getUsername(String email) {
        return email.split("@")[0];
    }

}
