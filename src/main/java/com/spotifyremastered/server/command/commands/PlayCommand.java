package com.spotifyremastered.server.command.commands;


import com.spotifyremastered.server.Client;
import com.spotifyremastered.server.SpotifyManager;
import com.spotifyremastered.server.command.Command;
import com.spotifyremastered.server.music.song.Song;
import com.spotifyremastered.server.music.song.SongStreamer;

public class PlayCommand extends Command {

    public static final String COMMAND_INFO = "play <songName>";
    public static final String COMMAND_DESCRIPTION = "Play a song.";

    public PlayCommand(String[] arguments, Client client, SpotifyManager spotifyManager) {
        super(arguments, client, spotifyManager);
    }

    @Override
    public String call() {
        String[] args = getArguments();

        if (isClientNotLoggedIn(getClient())) {
            return "User is not logged in.";
        }

        if (areArgumentsNotMinimum(getArguments(), 1)) {
            throw new IllegalArgumentException(String.format(INVALID_ARGS_MESSAGE_FORMAT, String.join(" ", args),
                    "play <songName>"));
        }

        String s = String.join(" ", args);

        Song toPlay = getSpotifyManager().getMusicManager().getSongManager().findSong(s);

        if (!getSpotifyManager().isPortAvailable(getClient())) {
            return "Song is already playing.";
        }

        int streamingPort = getSpotifyManager().getNewPort(getClient());

        SongStreamer streamer = new SongStreamer(getSpotifyManager(), getClient(), toPlay, streamingPort);
        Thread thread = new Thread(streamer, "Song Streamer for User: " + getClient().getEmail());
        thread.setDaemon(true);
        thread.start();

        return "play " + toPlay.getSongName() + " " + toPlay.getAudioFormatString() + " " + streamingPort;
    }

    public static PlayCommand of(String[] args, Client client, SpotifyManager spotifyManager) {
        return new PlayCommand(args, client, spotifyManager);
    }
}


