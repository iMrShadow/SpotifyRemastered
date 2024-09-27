package spotify.server.command.commands;

import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;

public class LogoutCommand extends Command {

    public static final String COMMAND_INFO = "logout/disconnect";
    public static final String COMMAND_DESCRIPTION = "Logout from Spotify.";

    public LogoutCommand(String[] arguments, Client client, SpotifyManager spotifyManager) {
        super(arguments, client, spotifyManager);
    }

    @Override
    public String call() {
        String[] args = getArguments();

        if (isClientNotLoggedIn(getClient())) {
            return "You are not logged in.";
        }

        if (areArgumentsNotValid(args, 0)) {
            throw new IllegalArgumentException(String.format(INVALID_ARGS_MESSAGE_FORMAT,
                    String.join(" ", args), "disconnect"));
        }

        String username = getClient().getEmail();
        getClient().setUserToGuest();
        return getSpotifyManager().getUserManager().logoutUser(username);
    }

    public static LogoutCommand of(String[] args, Client client, SpotifyManager spotifyManager) {
        return new LogoutCommand(args, client, spotifyManager);
    }
}
