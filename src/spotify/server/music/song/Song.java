package spotify.server.music.song;

import spotify.server.exceptions.SongNotFoundException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Song {

    private final String artist;
    private final String name;
    private static final String SONGS_PATH = "songs/";
    private static final String SPLIT_STRING = " - ";

    private static final int ARTIST_INDEX = 0;
    private static final int SONG_NAME_INDEX = 1;
    private static final int WAV_LENGTH = 4;
    private static final String WAV = ".wav";

    private final AudioFormat.Encoding encoding;
    private final float sampleRate;
    private final int sampleSizeInBits;
    private final int channels;
    private final int frameSize;
    private final float frameRate;
    private final boolean bigEndian;

    public Song(String artist, String name, AudioFormat audioFormat) {
        this.artist = artist;
        this.name = name;

        encoding = audioFormat.getEncoding();
        sampleRate = audioFormat.getSampleRate();
        sampleSizeInBits = audioFormat.getSampleSizeInBits();
        channels = audioFormat.getChannels();
        frameSize = audioFormat.getFrameSize();
        frameRate = audioFormat.getFrameRate();
        bigEndian = audioFormat.isBigEndian();
    }

    public static Song of(String fileName) throws SongNotFoundException {
        if (!fileName.endsWith(WAV)) {
            fileName = fileName + WAV;
        }

        String[] parts = fileName.split(SPLIT_STRING);

        String artist = parts[ARTIST_INDEX];
        String name = parts[SONG_NAME_INDEX].substring(0, parts[SONG_NAME_INDEX].length() - WAV_LENGTH);

        Song toReturn;
        try (AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(SONGS_PATH + fileName))) {

            AudioFormat audioFormat = inputStream.getFormat();
            toReturn = new Song(artist, name, audioFormat);

        } catch (UnsupportedAudioFileException | IOException e) {
            throw new SongNotFoundException("A Song with the Name: " + name + " does not exist");
        }

        return toReturn;
    }

    public String getPath() {
        return SONGS_PATH + getSongWavName();
    }

    public String name() {
        return name;
    }

    public String getAudioFormatString() {
        return encoding.toString() + " " + sampleRate + " " +
                sampleSizeInBits + " " + channels +
                " " + frameSize + " " + frameRate + " " + bigEndian;
    }

    public String getSongName() {
        return "\"" + artist + " - " + name + "\"";
    }

    public String getSongWavName() {
        return artist + " - " + name + ".wav";
    }

    public int getFrameSize() {
        return frameSize;
    }
}
