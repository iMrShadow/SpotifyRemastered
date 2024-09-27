package spotify.server.command;

import org.junit.jupiter.api.Test;
import spotify.server.command.commands.AddSongToCommand;
import spotify.server.command.commands.CreatePlaylistCommand;
import spotify.server.command.commands.LoginCommand;
import spotify.server.command.commands.LogoutCommand;
import spotify.server.command.commands.PlayCommand;
import spotify.server.command.commands.RegisterCommand;
import spotify.server.command.commands.SearchCommand;
import spotify.server.command.commands.ShowPlaylistCommand;
import spotify.server.command.commands.StopCommand;
import spotify.server.command.commands.TopCommand;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandDescriptionsTest {

    @Test
    void testGetCommandDescriptions() {
        Map<String, String> commandDescriptions = CommandDescriptions.getCommandDescriptions();

        assertEquals("Register to Spotify.", commandDescriptions.get(RegisterCommand.COMMAND_INFO));
        assertEquals("Login to Spotify.", commandDescriptions.get(LoginCommand.COMMAND_INFO));
        assertEquals("Logout from Spotify.", commandDescriptions.get(LogoutCommand.COMMAND_INFO));
        assertEquals("Search for songs.", commandDescriptions.get(SearchCommand.COMMAND_INFO));
        assertEquals("Shows the most popular songs listened at the moment.",
                commandDescriptions.get(TopCommand.COMMAND_INFO));
        assertEquals("Create a new playlist.", commandDescriptions.get(CreatePlaylistCommand.COMMAND_INFO));
        assertEquals("Add a song to a playlist.", commandDescriptions.get(AddSongToCommand.COMMAND_INFO));
        assertEquals("Show a playlist.", commandDescriptions.get(ShowPlaylistCommand.COMMAND_INFO));
        assertEquals("Play a song.", commandDescriptions.get(PlayCommand.COMMAND_INFO));
        assertEquals("Stop the song.", commandDescriptions.get(StopCommand.COMMAND_INFO));
    }
}

