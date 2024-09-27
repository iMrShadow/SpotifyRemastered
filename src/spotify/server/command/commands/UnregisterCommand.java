package spotify.server.command.commands;

import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;

public class UnregisterCommand extends Command {

    public static final String COMMAND_INFO = "unregister";
    public static final String COMMAND_DESCRIPTION = "Unregister from Spotify.";

    public UnregisterCommand(String[] arguments, Client client, SpotifyManager spotifyManager) {
        super(arguments, client, spotifyManager);
    }

    @Override
    public String call() {
        String[] args = getArguments();

        if (isClientNotLoggedIn(getClient())) {
            return "User is not logged in.";
        }

        if (areArgumentsNotValid(getArguments(), 0)) {
            throw new IllegalArgumentException(String.format(INVALID_ARGS_MESSAGE_FORMAT, String.join(" ", args),
                    "unregister"));
        }

        getClient().setUserToGuest();
        return super.getSpotifyManager().getUserManager().unregisterUser(getClient().getEmail());
    }

    public static UnregisterCommand of(String[] args, Client client, SpotifyManager spotifyManager) {
        return new UnregisterCommand(args, client, spotifyManager);
    }
}
