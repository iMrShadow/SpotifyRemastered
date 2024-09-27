package spotify.server.command.commands;

import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;
import spotify.server.music.playlist.PlaylistManager;

public class CreatePlaylistCommand extends Command {

    public static final String COMMAND_INFO = "create-playlist <playlistName>";
    public static final String COMMAND_DESCRIPTION = "Create a new playlist.";

    public CreatePlaylistCommand(String[] arguments, Client client, SpotifyManager spotifyManager) {
        super(arguments, client, spotifyManager);
    }

    @Override
    public String call() {
        String[] args = getArguments();

        if (isClientNotLoggedIn(getClient())) {
            return "User is not logged in.";
        }

        if (areArgumentsNotMinimum(getArguments(), 1)) {
            throw new IllegalArgumentException(String.format(INVALID_ARGS_MESSAGE_FORMAT,
                    String.join(" ", args), COMMAND_INFO));
        }

        String playlistName = String.join(" ", getArguments());
        PlaylistManager playlistManager = getSpotifyManager().getMusicManager().getPlayListManager();

        return playlistManager.createPlaylist(getClient().getEmail(), playlistName);
    }

    public static CreatePlaylistCommand of(String[] args, Client client, SpotifyManager spotifyManager) {
        return new CreatePlaylistCommand(args, client, spotifyManager);
    }
}
