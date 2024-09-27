package spotify.server.music.song;

import spotify.server.exceptions.SongNotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SongManager implements SongManagerAPI {

    private static final String SONGS_FOLDER_PATH = "songs/";

    private static final int FIRST_SONG_INDEX = 0;
    private final List<Song> availableSongs;
    private final Map<Song, Integer> songsPlayingNow;
    private final Object songsLock = new Object();

    public SongManager() {
        this.availableSongs = loadAvailableSongs();
        this.songsPlayingNow = availableSongs.stream()
                .collect(Collectors.toMap(song -> song, song -> 0));
    }

    public Map<Song, Integer> getSongsPlayingNow() {
        return songsPlayingNow;
    }

    public List<Song> getAvailableSongs() {
        return availableSongs;
    }

    @Override
    public List<Song> searchSongs(String keyword) {
        List<Song> matchingSongs = new ArrayList<>();

        if (isQuote(keyword)) {
            keyword = removeQuotes(keyword);
        }

        String[] keywords = keyword.toLowerCase().split("\\s+");

        for (Song song : availableSongs) {
            boolean allKeywordsMatched = true;
            String songName = song.getSongName();
            songName = removeQuotes(songName);
            for (String kw : keywords) {
                if (isQuote(kw)) {
                    kw = removeQuotes(kw);
                }
                if (!songName.toLowerCase().contains(kw)) {
                    allKeywordsMatched = false;
                    break;
                }
            }

            if (allKeywordsMatched) {
                matchingSongs.add(song);
            }
        }

        return matchingSongs;
    }

    @Override
    public List<Song> top(int number) {
        synchronized (songsLock) {
            if (number <= 0 || number > availableSongs.size()) {
                return Collections.emptyList();
            }

            Stream<Map.Entry<Song, Integer>> entryStream = songsPlayingNow.entrySet().stream();

            List<Map.Entry<Song, Integer>> sortedEntries = entryStream
                    .sorted(Map.Entry.<Song, Integer>comparingByValue().reversed())
                    .collect(Collectors.toList());

            List<Map.Entry<Song, Integer>> topEntries = sortedEntries
                    .subList(0, Math.min(number, sortedEntries.size()));

            return topEntries.stream()
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Song findSong(String songName) throws SongNotFoundException {
        synchronized (songsLock) {
            List<Song> songs = searchSongs(songName);
            if (songs.isEmpty()) {
                throw new SongNotFoundException("No songs found with name: " + songName);
            }

            return songs.get(FIRST_SONG_INDEX);
        }
    }

    private List<Song> loadAvailableSongs() throws SongNotFoundException {
        createMusicFolderIfNotExists();

        List<Song> songs = new ArrayList<>();
        File songsFolder = new File(SONGS_FOLDER_PATH);

        if (!(songsFolder.exists() && songsFolder.isDirectory())) {
            throw new SongNotFoundException("Songs folder does not exist or is not a directory.");
        }

        File[] files = songsFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".wav")) {
                    songs.add(Song.of(file.getName()));
                }
            }
        }

        return songs;
    }

    public void addSongToPlayingNow(Song song) {
        synchronized (songsLock) {
            songsPlayingNow.put(song, songsPlayingNow.getOrDefault(song, 0) + 1);
        }
    }

    public void removeSongToPlayingNow(Song song) {
        synchronized (songsLock) {
            songsPlayingNow.put(song, songsPlayingNow.getOrDefault(song, 0) - 1);
        }
    }

    private void createMusicFolderIfNotExists() {
        File songsFolder = new File(SONGS_FOLDER_PATH);
        if (!songsFolder.exists()) {
            if (songsFolder.mkdirs()) {
                System.out.println("Music folder created: " + songsFolder.getAbsolutePath());
            } else {
                System.err.println("Failed to create music folder.");
            }
        }
    }

    private boolean isQuote(String string) {
        return string.startsWith("\"") && string.endsWith("\"");
    }

    private String removeQuotes(String string) {
        return string.substring(1, string.length() - 1);
    }

}
