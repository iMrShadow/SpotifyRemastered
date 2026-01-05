package com.spotifyremastered.server;

import com.spotifyremastered.server.music.MusicManager;
import com.spotifyremastered.server.user.UserManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpotifyManager {

    private final UserManager userManager;
    private final MusicManager musicManager;
    private final Map<Client, Integer> portsManager = new ConcurrentHashMap<>();

    private static final short FIRST_AVAILABLE_PORT = 1338;
    private static final short MAX_PORTS = 1000;

    public SpotifyManager() {
        this.userManager = new UserManager(new HashMap<>(), new HashMap<>(), UserManager.USER_DATA_FILE_PATH);
        this.musicManager = new MusicManager();
    }

    public SpotifyManager(UserManager userManager, MusicManager musicManager) {
        this.userManager = userManager;
        this.musicManager = musicManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    public Map<Client, Integer> getPortsManager() {
        return Map.copyOf(portsManager);
    }

    public int getNewPort(Client client) {
        Integer existingPort = portsManager.get(client);
        if (existingPort != null) {
            return existingPort;
        }

        int maxPort = FIRST_AVAILABLE_PORT + MAX_PORTS;
        for (int i = FIRST_AVAILABLE_PORT; i < maxPort; i++) {
            if (isPortAvailable(i)) {
                Integer previous = portsManager.putIfAbsent(client, i);
                if (previous == null) {
                    return i;
                } else {
                    return previous;
                }
            }
        }
        throw new RuntimeException("No available ports");
    }

    public void removePort(Client client) {
        portsManager.remove(client);
    }

    public boolean isPortAvailable(int port) {
        return !portsManager.containsValue(port);
    }

    public boolean isPortAvailable(Client client) {
        return portsManager.containsKey(client);
    }
}
