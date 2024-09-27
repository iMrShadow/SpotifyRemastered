package spotify.server.command.commands;

import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;

public class StopCommand extends Command {

    public static final String COMMAND_INFO = "stop";
    public static final String COMMAND_DESCRIPTION = "Stop the song.";

    public StopCommand(String[] arguments, Client client, SpotifyManager spotifyManager) {
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
                    "stop"));
        }

        return "Stopped playing.";
    }

    public static StopCommand of(String[] args, Client client, SpotifyManager spotifyManager) {
        return new StopCommand(args, client, spotifyManager);
    }
}
