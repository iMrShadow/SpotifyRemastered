package spotify.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spotify.server.music.MusicManager;
import spotify.server.user.UserManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpotifyManagerTest {

    @Mock
    private Client mockClient;

    @Mock
    private UserManager mockedUserManager;

    @Mock
    private MusicManager mockedMusicManager;

    @InjectMocks
    private SpotifyManager spotifyManager;

    private static final short FIRST_AVAILABLE_PORT = 1338;
    private static final short TEST_PORT = 2000;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        spotifyManager = mock(SpotifyManager.class);

        when(spotifyManager.getUserManager()).thenReturn(mockedUserManager);
        when(spotifyManager.getMusicManager()).thenReturn(mockedMusicManager);
        when(mockClient.getEmail()).thenReturn("user@example.com");
        // spotifyManager = new SpotifyManager(mockedUserManager, mockedMusicManager);
    }

    @Test
    void testGetNewPort() {
        when(spotifyManager.isPortAvailable(FIRST_AVAILABLE_PORT)).thenReturn(true);

        SpotifyManager spotifyManager = new SpotifyManager(mockedUserManager, mockedMusicManager);

        assertEquals(FIRST_AVAILABLE_PORT, spotifyManager.getNewPort(mockClient));
        assertEquals(FIRST_AVAILABLE_PORT, spotifyManager.getPortsManager().get(mockClient));
    }

    @Test
    void testRemovePort() {
        spotifyManager.getPortsManager().put(mockClient, (int) FIRST_AVAILABLE_PORT);

        spotifyManager.removePort(mockClient);

        SpotifyManager spotifyManager = new SpotifyManager(mockedUserManager, mockedMusicManager);
        assertFalse(spotifyManager.getPortsManager().containsKey(mockClient));
    }

    @Test
    void testIsPortAvailable() {
        spotifyManager.getPortsManager().put(mockClient, (int) TEST_PORT);

        when(spotifyManager.isPortAvailable(TEST_PORT)).thenReturn(true);
        assertTrue(spotifyManager.isPortAvailable(TEST_PORT));

        spotifyManager.getPortsManager().remove(mockClient, (int) TEST_PORT);
    }

    @Test
    void testIsPortNotAvailable() {
        when(spotifyManager.isPortAvailable(TEST_PORT)).thenReturn(false);

        assertFalse(spotifyManager.isPortAvailable(TEST_PORT));
    }

    @Test
    void testIsPortNotAvailableForClient() {
        spotifyManager.getPortsManager().put(mockClient, (int) TEST_PORT);
        when(spotifyManager.isPortAvailable(TEST_PORT)).thenReturn(false);
        SpotifyManager spotifyManager = new SpotifyManager(mockedUserManager, mockedMusicManager);

        assertFalse(spotifyManager.isPortAvailable(mockClient));
    }
}
