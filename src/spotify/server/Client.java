package spotify.server;

import java.nio.channels.SocketChannel;
import spotify.server.user.User;

public class Client {

    /*
     * The client is a user that is connected to the server, but not necessarily logged in.
     * They are guests until they log in.
     *
     * @param  socketChannel  description of parameter
     * @param  user           description of parameter
     * @return                description of return value
     */

    private final SocketChannel socketChannel;
    private User user;

    public Client(SocketChannel socketChannel, User user) {
        this.socketChannel = socketChannel;
        this.user = user;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public String getEmail() {
        return user.email();
    }

    public void setUser(String email, String password) {
        user = new User(email, password);
    }

    public void setUserToGuest() {
        user = new User("Guest", "Guest");
    }
}


