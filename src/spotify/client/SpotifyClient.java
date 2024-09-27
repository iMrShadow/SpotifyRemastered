package spotify.client;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Scanner;

public class SpotifyClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1337;
    private static final int BUFFER_SIZE = 4096;
    private static final String CLIENT_LOG_FILE_NAME = "client_error_logs.txt";

    private final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    private static final int COMMAND_INDEX = 0;
    private static final int SONG_INDEX = 1;
    private static final int ENCODING_INDEX = 2;
    private static final int SAMPLE_RATE_INDEX = 3;
    private static final int SAMPLE_SIZE_IN_BITS_INDEX = 4;
    private static final int CHANNELS_INDEX = 5;
    private static final int FRAME_SIZE_INDEX = 6;
    private static final int FRAME_RATE_INDEX = 7;
    private static final int BIG_ENDIAN_INDEX = 8;
    private static final int PORT_INDEX = 9;

    private SourceDataLine dataLine;
    private boolean connected = true;

    public static void main(String[] args) {
        SpotifyClient client = new SpotifyClient();
        client.startClient();
    }

    private void startClient() {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            System.out.println("Connected to the server. Type 'help' for available commands.");

            while (connected) {
                System.out.print("> ");
                String message = scanner.nextLine();
                if (message.equals("quit")) {
                    System.out.println("Quitting client...");
                    connected = false;
                    break;
                }

                sendMessageToServer(socketChannel, message);
                receiveAndProcessServerReply(socketChannel, message);
            }
        } catch (IOException e) {
            logTechnicalDetails("Couldn't connect to the server. " +
                    "Try again later or contact administrator by providing the logs in " +
                    Path.of(CLIENT_LOG_FILE_NAME).toAbsolutePath(), e);
        }
    }

    private void sendMessageToServer(SocketChannel socketChannel, String message) throws IOException {
        System.out.println("Sending message <" + message + "> to the server...");

        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        socketChannel.write(buffer);
    }

    private void receiveAndProcessServerReply(SocketChannel socketChannel, String sentMessage) throws IOException {
        buffer.clear();
        socketChannel.read(buffer);
        buffer.flip();

        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        String reply = new String(byteArray, StandardCharsets.UTF_8);

        processCommand(sentMessage, reply);
    }

    private void processCommand(String message, String reply) {
        if (message.startsWith("play")) {
            playSong(reply);
        } else if (message.equals("stop")) {
            stopSong();
            System.out.println(reply);
        } else if (message.equals("disconnect") || message.equals("logout")) {
            System.out.println(reply);
            stopSong();
        } else {
            System.out.println(reply);
        }
    }

    private void playSong(String reply) {
        String[] splitReply = reply.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        if (!splitReply[COMMAND_INDEX].equals("play")) {
            return;
        }

        String song = splitReply[SONG_INDEX];

        AudioFormat audioFormat = new AudioFormat(new AudioFormat.Encoding(splitReply[ENCODING_INDEX]),
                Float.parseFloat(splitReply[SAMPLE_RATE_INDEX]),
                Integer.parseInt(splitReply[SAMPLE_SIZE_IN_BITS_INDEX]),
                Integer.parseInt(splitReply[CHANNELS_INDEX]), Integer.parseInt(splitReply[FRAME_SIZE_INDEX]),
                Float.parseFloat(splitReply[FRAME_RATE_INDEX]), Boolean.parseBoolean(splitReply[BIG_ENDIAN_INDEX]));
        Line.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

        int streamingPort = Integer.parseInt(splitReply[PORT_INDEX]);

        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open();
            System.out.println("Playing Song: " + song);

            Thread.ofVirtual().start(new Player(streamingPort, dataLine));
        } catch (LineUnavailableException e) {
            logTechnicalDetails("Something went wrong with the audio line. " +
                    "Please contact the developer.", e);
        }
    }

    private void stopSong() {
        if (dataLine == null) {
            System.out.println("Nothing to stop");
            return;
        }

        dataLine.stop();
        dataLine.drain();
        dataLine.close();
    }

    private void logTechnicalDetails(String errorMessage, Exception e) {
        System.out.println(errorMessage);
        logTechnicalDetailsToFile(e);
    }

    private void logTechnicalDetailsToFile(Exception e) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CLIENT_LOG_FILE_NAME))) {
            writer.println("Timestamp: " + LocalDateTime.now());
            writer.println("Error Details: " + e.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            writer.println("Stack Trace:\n" + sw);
            writer.println("------------------------------------------");
        } catch (IOException ex) {
            System.err.println("Error writing technical details to file: " + ex.getMessage());
            throw new RuntimeException(e);
        }
    }
}