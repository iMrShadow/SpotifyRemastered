package spotify.server.command.commands;

import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginCommand extends Command {

    private static final byte EMAIL_INDEX = 0;
    private static final byte PASSWORD_INDEX = 1;

    public static final String COMMAND_INFO = "login <username> <password>";
    public static final String COMMAND_DESCRIPTION = "Login to Spotify.";

    public LoginCommand(String[] arguments, Client client, SpotifyManager spotifyManager) {
        super(arguments, client, spotifyManager);
    }

    @Override
    public String call() {
        String[] args = getArguments();

        if (areArgumentsNotValid(args, 2)) {
            throw new IllegalArgumentException(String.format(INVALID_ARGS_MESSAGE_FORMAT, String.join(" ", args),
                    "login <username> <password>"));
        }

        String userEmail = args[EMAIL_INDEX];
        String password = args[PASSWORD_INDEX];

        if (!validateEmail(userEmail)) {
            throw new IllegalArgumentException("Invalid email.");
        }

        getClient().setUser(userEmail, password);
        return getSpotifyManager().getUserManager().loginUser(userEmail, password);
    }

    private boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static LoginCommand of(String[] args, Client client, SpotifyManager spotifyManager) {
        return new LoginCommand(args, client, spotifyManager);
    }
}
