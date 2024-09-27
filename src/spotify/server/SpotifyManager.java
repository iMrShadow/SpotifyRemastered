package spotify.server;

import spotify.server.music.MusicManager;
import spotify.server.user.UserManager;

import java.util.HashMap;
import java.util.Map;

public class SpotifyManager {

    private final UserManager userManager;
    private final MusicManager musicManager;
    private final Map<Client, Integer> portsManager;
    private final Object lock = new Object();

    private static final short FIRST_AVAILABLE_PORT = 1338;
    private static final short MAX_PORTS = 1000;

    public SpotifyManager() {
        this.userManager = new UserManager(new HashMap<>(), new HashMap<>(), UserManager.USER_DATA_FILE_PATH);
        this.musicManager = new MusicManager();
        this.portsManager = new HashMap<>();
    }

    public SpotifyManager(UserManager userManager, MusicManager musicManager) {
        this.userManager = userManager;
        this.musicManager = musicManager;
        this.portsManager = new HashMap<>();
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    public Map<Client, Integer> getPortsManager() {
        synchronized (lock) {
            return new HashMap<>(portsManager);
        }
    }

    public int getNewPort(Client client) {
        synchronized (lock) {
            int maxPort = FIRST_AVAILABLE_PORT + MAX_PORTS;
            for (int i = FIRST_AVAILABLE_PORT; i < maxPort; i++) {
                if (isPortAvailable(i)) {
                    portsManager.put(client, i);
                    return i;
                }
            }
            throw new RuntimeException("No available ports");
        }
    }

    public void removePort(Client client) {
        synchronized (lock) {
            portsManager.remove(client);
        }
    }

    public boolean isPortAvailable(int port) {
        synchronized (lock) {
            return !portsManager.containsValue(port);
        }
    }

    public boolean isPortAvailable(Client client) {
        synchronized (lock) {
            return portsManager.containsValue(portsManager.get(client));
        }
    }
}
