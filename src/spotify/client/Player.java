package spotify.client;

import javax.sound.sampled.SourceDataLine;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

public class Player implements Runnable {

    final int port;
    SourceDataLine dataLine;

    public Player(int port, SourceDataLine dataLine) {
        this.port = port;
        this.dataLine = dataLine;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", port);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream())) {

            byte[] toWrite = new byte[dataLine.getFormat().getFrameSize()];
            dataLine.start();
            try {
                do {
                    int readBytes = bufferedInputStream.read(toWrite, 0, toWrite.length);

                    if (readBytes == -1) {
                        break;
                    }

                    dataLine.write(toWrite, 0, readBytes);
                } while (dataLine.isOpen());

            } catch (IllegalArgumentException ignored) {
                System.out.println("Song has ended!");
            }
        } catch (IOException ignored) {
            System.out.println("No Song Streamer detected");
        }

        dataLine.drain();
        dataLine.close();

        dataLine = null;
    }
}