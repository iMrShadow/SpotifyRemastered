package spotify.server.music.playlist;

import spotify.server.music.song.Song;

import java.util.List;

public interface PlaylistManagerAPI {

    /**
     * A description of the entire Java function.
     *
     * @param  username      description of parameter
     * @param  playlistName  description of parameter
     * @return               description of return value
     */
    String createPlaylist(String username, String playlistName);

    /**
     * Adds a song to a playlist for a given user.
     *
     * @param  username      the username of the user
     * @param  playlistName  the name of the playlist
     * @param  song          the song to be added
     * @return               the updated playlist after adding the song
     */
    String addSongToPlaylist(String username, String playlistName, Song song);

    /**
     * Retrieves a playlist for the given username.
     *
     * @param  username      the username of the user
     * @param  playlistName  the name of the playlist
     * @return               a list of strings representing the playlist
     */
    List<String> showPlaylist(String username, String playlistName);
}
