package com.spotifyremastered.server.command;

import com.spotifyremastered.server.command.commands.AddSongToCommand;
import com.spotifyremastered.server.command.commands.CreatePlaylistCommand;
import com.spotifyremastered.server.command.commands.LoginCommand;
import com.spotifyremastered.server.command.commands.LogoutCommand;
import com.spotifyremastered.server.command.commands.PlayCommand;
import com.spotifyremastered.server.command.commands.RegisterCommand;
import com.spotifyremastered.server.command.commands.SearchCommand;
import com.spotifyremastered.server.command.commands.ShowPlaylistCommand;
import com.spotifyremastered.server.command.commands.StopCommand;
import com.spotifyremastered.server.command.commands.TopCommand;
import org.junit.jupiter.api.Test;

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

