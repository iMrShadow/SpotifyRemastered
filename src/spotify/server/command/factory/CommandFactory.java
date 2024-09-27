package spotify.server.command.factory;

import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;
import spotify.server.command.commands.AddSongToCommand;
import spotify.server.command.commands.CreatePlaylistCommand;
import spotify.server.command.commands.HelpCommand;
import spotify.server.command.commands.LoginCommand;
import spotify.server.command.commands.LogoutCommand;
import spotify.server.command.commands.PlayCommand;
import spotify.server.command.commands.RegisterCommand;
import spotify.server.command.commands.SearchCommand;
import spotify.server.command.commands.ShowPlaylistCommand;
import spotify.server.command.commands.StopCommand;
import spotify.server.command.commands.TopCommand;
import spotify.server.command.commands.UnregisterCommand;

public class CommandFactory {
    private static final int COMMAND_TYPE_INDEX = 0;

    public static Command create(String clientInput, Client client, SpotifyManager spotifyManager) {
        if (clientInput == null || clientInput.isBlank()) {
            throw new IllegalArgumentException("Client input cannot be null or blank.");
        }
        String[] args = clientInput.trim().split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        String command = args[COMMAND_TYPE_INDEX].toLowerCase();

        return switch (command) {
            case "help" -> HelpCommand.of(args, client, spotifyManager);
            case "register" -> RegisterCommand.of(args, client, spotifyManager);
            case "unregister" -> UnregisterCommand.of(args, client, spotifyManager);
            case "login" -> LoginCommand.of(args, client, spotifyManager);
            case "logout", "disconnect" -> LogoutCommand.of(args, client, spotifyManager);
            case "search" -> SearchCommand.of(args, client, spotifyManager);
            case "top" -> TopCommand.of(args, client, spotifyManager);
            case "create-playlist" -> CreatePlaylistCommand.of(args, client, spotifyManager);
            case "add-song-to" -> AddSongToCommand.of(args, client, spotifyManager);
            case "show-playlist" -> ShowPlaylistCommand.of(args, client, spotifyManager);
            case "play" -> PlayCommand.of(args, client, spotifyManager);
            case "stop" -> StopCommand.of(args, client, spotifyManager);
            default -> null;
        };
    }
}
