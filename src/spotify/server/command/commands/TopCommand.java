package spotify.server.command.commands;

import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;
import spotify.server.music.song.Song;

import java.util.List;

public class TopCommand extends Command {

    public static final String COMMAND_INFO = "top <number>";
    public static final String COMMAND_DESCRIPTION = "Shows the most popular songs listened at the moment.";

    public TopCommand(String[] arguments, Client client, SpotifyManager spotifyManager) {
        super(arguments, client, spotifyManager);
    }

    @Override
    public String call() {
        String[] args = getArguments();

        if (isClientNotLoggedIn(getClient())) {
            return "User is not logged in.";
        }

        if (areArgumentsNotValid(args, 1)) {
            throw new IllegalArgumentException(String.format(INVALID_ARGS_MESSAGE_FORMAT, String.join(" ", args),
                    "top <number>"));
        }

        if (!isInteger(args[0])) {
            throw new IllegalArgumentException("Invalid number.");
        }

        int number = Integer.parseInt(args[0]);

        List<Song> playlist = getSpotifyManager().getMusicManager().getSongManager().top(number);

        return formatTopSongs(playlist);
    }

    public static TopCommand of(String[] args, Client client, SpotifyManager spotifyManager) {
        return new TopCommand(args, client, spotifyManager);
    }

    private String formatTopSongs(List<Song> playlist) {
        StringBuilder result = new StringBuilder("Top Songs:\n");
        int counter = 0;
        for (Song song : playlist) {
            result.append(++counter).append(". ").append(song.getSongName()).append("\n");
        }
        result.delete(result.length() - 1, result.length());

        return result.toString();
    }

    public static boolean isInteger(String strInput) {
        boolean isInteger = true;
        try {
            Integer.parseInt(strInput);
        } catch (NumberFormatException e) {
            isInteger = false;
        }

        return isInteger;
    }
}
