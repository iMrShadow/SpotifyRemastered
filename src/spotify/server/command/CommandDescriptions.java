package spotify.server.command;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandDescriptions {
    private static final Map<String, String> COMMAND_DESCRIPTIONS = new HashMap<>();

    static {
        COMMAND_DESCRIPTIONS.put(RegisterCommand.COMMAND_INFO, RegisterCommand.COMMAND_DESCRIPTION);
        COMMAND_DESCRIPTIONS.put(LoginCommand.COMMAND_INFO, LoginCommand.COMMAND_DESCRIPTION);
        COMMAND_DESCRIPTIONS.put(LogoutCommand.COMMAND_INFO, LogoutCommand.COMMAND_DESCRIPTION);
        COMMAND_DESCRIPTIONS.put(SearchCommand.COMMAND_INFO, SearchCommand.COMMAND_DESCRIPTION);
        COMMAND_DESCRIPTIONS.put(TopCommand.COMMAND_INFO, TopCommand.COMMAND_DESCRIPTION);
        COMMAND_DESCRIPTIONS.put(CreatePlaylistCommand.COMMAND_INFO, CreatePlaylistCommand.COMMAND_DESCRIPTION);
        COMMAND_DESCRIPTIONS.put(AddSongToCommand.COMMAND_INFO, AddSongToCommand.COMMAND_DESCRIPTION);
        COMMAND_DESCRIPTIONS.put(ShowPlaylistCommand.COMMAND_INFO, ShowPlaylistCommand.COMMAND_DESCRIPTION);
        COMMAND_DESCRIPTIONS.put(PlayCommand.COMMAND_INFO, PlayCommand.COMMAND_DESCRIPTION);
        COMMAND_DESCRIPTIONS.put(StopCommand.COMMAND_INFO, StopCommand.COMMAND_DESCRIPTION);
    }

    public static Map<String, String> getCommandDescriptions() {
        return Collections.unmodifiableMap(COMMAND_DESCRIPTIONS);
    }
}
