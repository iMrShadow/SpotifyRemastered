package spotify.server.command;

import spotify.server.Client;
import spotify.server.SpotifyManager;

import java.util.Arrays;
import java.util.concurrent.Callable;

public abstract class Command implements Callable<String> {

    private String[] arguments;
    private final Client client;
    private final SpotifyManager spotifyManager;

    protected static final String INVALID_ARGS_MESSAGE_FORMAT = "Invalid arguments: \"%s\". Example: \"%s\"";

    public Command(String[] arguments, Client client, SpotifyManager spotifyManager) {
        this.arguments = arguments;
        this.client = client;
        this.spotifyManager = spotifyManager;
    }

    protected SpotifyManager getSpotifyManager() {
        return spotifyManager;
    }

    protected Client getClient() {
        return client;
    }

    public String[] getArguments() {
        if (arguments == null || arguments.length <= 1) {
            return new String[0];
        }

        return Arrays.copyOfRange(arguments, 1, arguments.length);
    }

    /**
     * Check if the arguments are not valid.
     *
     * @param  args          the array of arguments
     * @param  expectedArgs  the expected number of arguments
     * @return               true if the arguments are not valid, false otherwise
     */
    protected boolean areArgumentsNotValid(String[] args, int expectedArgs) {
        if (args == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        return args.length != expectedArgs;
    }

    /**
     * Checks if the arguments are not minimum.
     *
     * @param  args          the arguments to check
     * @param  expectedArgs  the expected number of arguments
     * @return               true if the arguments are not minimum, false otherwise
     */
    protected boolean areArgumentsNotMinimum(String[] args, int expectedArgs) {
        if (args == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        return args.length < expectedArgs;
    }

    protected boolean isClientNotLoggedIn(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }

        return !spotifyManager.getUserManager().isUserLoggedIn(client.getEmail());
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }
}