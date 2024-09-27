package spotify.server.music;

import spotify.server.exceptions.SongNotFoundException;
import spotify.server.music.playlist.PlaylistManager;
import spotify.server.music.song.SongManager;

public class MusicManager {

    private final SongManager songManager;
    private final PlaylistManager playListManager;

    public MusicManager() throws SongNotFoundException {
        this.playListManager = new PlaylistManager();
        this.songManager = new SongManager();
    }

    public PlaylistManager getPlayListManager() {
        return playListManager;
    }

    public SongManager getSongManager() {
        return songManager;
    }
}
