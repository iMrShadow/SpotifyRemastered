package spotify.server.command.commands;

import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;
import spotify.server.music.MusicManager;
import spotify.server.music.song.Song;

public class AddSongToCommand extends Command {

    public static final String COMMAND_INFO = "add-song-to <playlistName> <songName>";
    public static final String COMMAND_DESCRIPTION = "Add a song to a playlist.";

    private static final int PLAYLIST_INDEX = 0;
    private static final int SONG_INDEX = 1;

    public AddSongToCommand(String[] arguments, Client client, SpotifyManager spotifyManager) {
        super(arguments, client, spotifyManager);
    }

    @Override
    public String call() {
        String[] args = getArguments();

        if (isClientNotLoggedIn(getClient())) {
            return "User is not logged in.";
        }

        if (areArgumentsNotMinimum(args, 2)) {
            throw new IllegalArgumentException(String.format(INVALID_ARGS_MESSAGE_FORMAT, String.join(" ", args),
                    "add-song-to <playlistName> <songName>"));
        }

        MusicManager musicManager = getSpotifyManager().getMusicManager();

        String playlistName = handleQuotes(args[PLAYLIST_INDEX]);
        String songString = handleQuotes(args[SONG_INDEX]);

        Song song = musicManager.getSongManager().findSong(songString);
        String username = getClient().getEmail();

        return musicManager.getPlayListManager().addSongToPlaylist(username, playlistName, song);
    }

    public static AddSongToCommand of(String[] args, Client client, SpotifyManager spotifyManager) {
        return new AddSongToCommand(args, client, spotifyManager);
    }

    private String handleQuotes(String argument) {
        if (isQuote(argument)) {
            return argument;
        } else {
            StringBuilder stringBuilder = new StringBuilder(argument);
            for (int i = 2; i < getArguments().length; i++) {
                stringBuilder.append(" ").append(getArguments()[i]);
            }
            return addQuotes(stringBuilder.toString());
        }
    }

    private boolean isQuote(String string) {
        return string.startsWith("\"") && string.endsWith("\"");
    }

    private String addQuotes(String string) {
        return "\"" + string + "\"";
    }
}
