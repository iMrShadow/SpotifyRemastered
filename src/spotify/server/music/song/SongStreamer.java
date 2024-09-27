package spotify.server.music.song;

import spotify.server.Client;
import spotify.server.SpotifyManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SongStreamer implements Runnable {

    private final SpotifyManager spotifyManager;
    private final Client client;
    private final Song song;
    private final int streamingPort;

    public SongStreamer(SpotifyManager spotifyManager, Client client, Song song, int streamingPort) {
        this.spotifyManager = spotifyManager;
        this.client = client;
        this.song = song;
        this.streamingPort = streamingPort;
    }

    @Override
    public void run() {
        spotifyManager.getMusicManager().getSongManager().addSongToPlayingNow(song);
        try (ServerSocket serverSocket = new ServerSocket(streamingPort)) {
            try (Socket socket = serverSocket.accept();
                 BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(
                         Path.of(song.getPath())))) {

                byte[] toWrite = new byte[song.getFrameSize()];
                while (bufferedInputStream.available() > 0) {
                    int readBytes = bufferedInputStream.read(toWrite, 0, toWrite.length);
                    outputStream.write(toWrite, 0, readBytes);
                }

                outputStream.flush();
            } catch (SocketException e) {
                System.out.println("SocketException: " + e.getMessage());
            } finally {
                spotifyManager.getMusicManager().getSongManager().removeSongToPlayingNow(song);
                spotifyManager.removePort(client);
            }
        } catch (IOException e) {
            throw new RuntimeException("A Problem occurred while streaming a song." + e);
        }
    }
}
