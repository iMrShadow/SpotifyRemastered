package spotify.server.music.playlist;

import spotify.server.exceptions.PlaylistNotFoundException;
import spotify.server.music.song.Song;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PlaylistManager implements PlaylistManagerAPI {

    private String playlistsDirectory = "playlists/";

    public PlaylistManager() {
        initializePlaylistsDirectory();
    }

    public PlaylistManager(String playlistsDirectory) {
        this.playlistsDirectory = playlistsDirectory;
        initializePlaylistsDirectory();
    }

    private void initializePlaylistsDirectory() {
        File directory = new File(playlistsDirectory);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Playlists directory created successfully.");
            } else {
                System.err.println("Failed to create playlists directory.");
            }
        }
    }

    @Override
    public String createPlaylist(String username, String playlistName) {
        String playlistFileName = buildPlaylistFileName(username);
        playlistName = removeQuotesIfNeeded(playlistName);

        try {
            File userPlaylistFile = createPlaylistFileIfNotExists(playlistFileName);

            if (playlistExistsInFile(userPlaylistFile, playlistName)) {
                return "Playlist already exists.";
            }

            appendToPlaylistFile(userPlaylistFile, playlistName);

            return "Playlist created successfully.";
        } catch (IOException e) {
            throw new RuntimeException("Error occurred during creation of playlist " + playlistFileName, e);
        }
    }

    private File createPlaylistFileIfNotExists(String playlistFileName) throws IOException {
        File userPlaylistFile = new File(playlistFileName);

        if (!userPlaylistFile.exists() && !userPlaylistFile.createNewFile()) {
            System.err.println("Failed to create playlist file.");
        }

        return userPlaylistFile;
    }

    private boolean playlistExistsInFile(File userPlaylistFile, String playlistName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(userPlaylistFile))) {
            return reader.lines()
                    .map(line -> Playlist.of(line).name())
                    .anyMatch(existingName -> existingName.equals(playlistName));
        }
    }

    private void appendToPlaylistFile(File userPlaylistFile, String playlistName) throws IOException {
        try (FileWriter writer = new FileWriter(userPlaylistFile, true)) {
            writer.write(playlistName + "::\n");
        }
    }

    @Override
    public String addSongToPlaylist(String username, String playlistName, Song song) {
        String playlistFileName = buildPlaylistFileName(username);
        playlistName = removeQuotesIfNeeded(playlistName);
        Path path = Path.of(playlistFileName);

        if (!Files.exists(path)) {
            return "Playlist not found.";
        }

        try {
            List<String> existingLines = Files.readAllLines(path);
            for (int i = 0; i < existingLines.size(); i++) {
                Playlist currentPlaylist = Playlist.of(existingLines.get(i));

                if (currentPlaylist.name().equals(playlistName)) {
                    List<String> existingSongs = currentPlaylist.songs();
                    if (!existingSongs.contains(song.getSongName())) {
                        existingLines.set(i, Playlist.toString(currentPlaylist) + "::" + song.getSongName());
                        Files.write(path, existingLines);
                        return "Song added to playlist.";
                    } else {
                        return "Song already exists in the playlist.";
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + playlistFileName, e);
        }
        return "Playlist not found.";
    }

    @Override
    public List<String> showPlaylist(String username, String playlistName) {
        String playlistFileName = buildPlaylistFileName(username);
        playlistName = removeQuotesIfNeeded(playlistName);

        Path path = Path.of(playlistFileName);

        if (!Files.exists(path)) {
            throw new PlaylistNotFoundException("User doesn't have playlists.");
        }

        try {
            List<String> existingLines = Files.readAllLines(path);

            for (String existingLine : existingLines) {
                Playlist currentPlaylist = Playlist.of(existingLine);
                if (currentPlaylist.name().equals(playlistName)) {
                    return currentPlaylist.songs();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + playlistFileName, e);
        }

        throw new PlaylistNotFoundException("Playlist not found.");
    }

    private String buildPlaylistFileName(String username) {
        return playlistsDirectory + File.separator + username + ".csv";
    }

    private String removeQuotesIfNeeded(String input) {
        if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        }

        return input;
    }
}
