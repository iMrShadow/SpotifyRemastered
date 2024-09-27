package spotify.server.user;

public record User(String email, String password) {

    public static User of(String email, String password) {
        return new User(email, password);
    }

}
