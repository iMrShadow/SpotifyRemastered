package spotify.server.command.commands;

import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;
import spotify.server.music.song.Song;

import java.util.List;

public class SearchCommand extends Command {

    public static final String COMMAND_INFO = "search <songName>";
    public static final String COMMAND_DESCRIPTION = "Search for songs.";

    public SearchCommand(String[] arguments, Client client, SpotifyManager spotifyManager) {
        super(arguments, client, spotifyManager);
    }

    @Override
    public String call() {
        String[] args = getArguments();

        if (isClientNotLoggedIn(getClient())) {
            return "User is not logged in.";
        }

        if (areArgumentsNotMinimum(args, 1)) {
            throw new IllegalArgumentException(String.format(INVALID_ARGS_MESSAGE_FORMAT, String.join(" ", args),
                    "search <songName>"));
        }

        String keywords = String.join(" ", args);

        List<Song> songs = getSpotifyManager().getMusicManager().getSongManager().searchSongs(keywords);
        return formatSearchedSongs(songs);
    }

    public static SearchCommand of(String[] args, Client client, SpotifyManager spotifyManager) {
        return new SearchCommand(args, client, spotifyManager);
    }

    private String formatSearchedSongs(List<Song> playlist) {
        StringBuilder result = new StringBuilder("Top Songs:\n");

        int counter = 0;
        for (Song song : playlist) {
            result.append(++counter).append(". ").append(song.getSongName()).append("\n");
        }

        return result.toString();
    }
}
