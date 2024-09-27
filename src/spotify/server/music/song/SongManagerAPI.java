package spotify.server.music.song;

import spotify.server.exceptions.SongNotFoundException;

import java.util.List;

public interface SongManagerAPI {

    /**
     * Search for songs by the given song name.
     *
     * @param songName the name of the song to search for
     * @return a list of songs matching the given name
     */
    List<Song> searchSongs(String songName);

    /**
     * Finds a song by the given name.
     *
     * @param songName the name of the song to find
     * @return the found Song object
     */
    Song findSong(String songName) throws SongNotFoundException;

    /**
     * Retrieves the top songs based on the specified number.
     *
     * @param number the number of top songs to retrieve
     * @return a list of the top songs
     */
    List<Song> top(int number);
}
