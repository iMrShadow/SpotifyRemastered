package spotify.server.command.executor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CommandExecutorTest {

    @Mock
    private Command mockCommand;

    @Mock
    private Client mockClient;

    @Mock
    private SpotifyManager mockSpotifyManager;

    @InjectMocks
    private CommandExecutor commandExecutor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteValidCommand() throws Exception {
        when(mockCommand.call()).thenReturn("Command executed successfully");

        String result = commandExecutor.execute(mockCommand);

        assertEquals("Command executed successfully", result);
        verify(mockCommand, times(1)).call();
        verifyNoMoreInteractions(mockCommand, mockClient, mockSpotifyManager);
    }

    @Test
    void testExecuteInvalidCommand() throws Exception {
        String result = commandExecutor.execute(null);

        assertEquals("Invalid command.", result);
        verifyNoInteractions(mockCommand, mockClient, mockSpotifyManager);
    }
}
