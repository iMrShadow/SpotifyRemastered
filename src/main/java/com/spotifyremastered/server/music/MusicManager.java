package com.spotifyremastered.server.music;

import com.spotifyremastered.server.exceptions.SongNotFoundException;
import com.spotifyremastered.server.music.playlist.PlaylistManager;
import com.spotifyremastered.server.music.song.SongManager;

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
