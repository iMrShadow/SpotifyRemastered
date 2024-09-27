package spotify.server.command.commands;

import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;

import java.util.List;

public class ShowPlaylistCommand extends Command {

    public static final String COMMAND_INFO = "show <playlistName>";
    public static final String COMMAND_DESCRIPTION = "Show a playlist.";

    public ShowPlaylistCommand(String[] arguments, Client client, SpotifyManager spotifyManager) {
        super(arguments, client, spotifyManager);
    }

    @Override
    public String call() {
        String[] args = getArguments();
        String username = getClient().getEmail();

        if (isClientNotLoggedIn(getClient())) {
            return "User is not logged in.";
        }

        if (areArgumentsNotMinimum(args, 1)) {
            throw new IllegalArgumentException(String.format(INVALID_ARGS_MESSAGE_FORMAT, String.join(" ", args),
                    "show <playlistName>"));
        }

        String playlistName = String.join(" ", args);
        List<String> playlist = getSpotifyManager()
                .getMusicManager()
                .getPlayListManager()
                .showPlaylist(username, playlistName);

        return printPlaylist(playlistName, playlist);
    }

    public static ShowPlaylistCommand of(String[] args, Client client, SpotifyManager spotifyManager) {
        return new ShowPlaylistCommand(args, client, spotifyManager);
    }

    private String printPlaylist(String playlistName, List<String> playlist) {
        if (playlist.isEmpty()) {
            return "Playlist is empty.";
        }

        StringBuilder result = new StringBuilder("Playlist: " + playlistName + "\n");

        int counter = 0;
        for (String song : playlist) {
            result.append(++counter).append(". ").append(song).append("\n");
        }
        result.delete(result.length() - 1, result.length());

        return result.toString();
    }
}
