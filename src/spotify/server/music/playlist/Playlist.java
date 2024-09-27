package spotify.server.music.playlist;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record Playlist(String name, List<String> songs) {

    public static Playlist of(String line) {
        if (line == null) {
            throw new NullPointerException("Playlist cannot be null.");
        }

        String[] args = line.split("::");
        String name = args[0];

        if (args.length == 1) {
            return new Playlist(name, Collections.emptyList());
        }

        String[] songArgs = Arrays.copyOfRange(args, 1, args.length);

        List<String> songList = Arrays.stream(songArgs)
                .toList();

        return new Playlist(name, songList);
    }

    public static String toString(Playlist playlist) {
        if (playlist.songs().isEmpty()) {
            return playlist.name();
        }

        return playlist.name() + "::" + String.join("::", playlist.songs());
    }
}
