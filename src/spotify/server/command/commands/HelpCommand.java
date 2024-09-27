package spotify.server.command.commands;

import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;
import spotify.server.command.CommandDescriptions;

import java.util.Map;

public class HelpCommand extends Command {

    public static final String COMMAND_INFO = "help";
    public static final String COMMAND_DESCRIPTION = "Shows all commands.";

    public HelpCommand(String[] arguments, Client client, SpotifyManager spotifyManager) {
        super(arguments, client, spotifyManager);
    }

    @Override
    public String call() {
        if (areArgumentsNotValid(getArguments(), 0)) {
            return "Unknown command.";
        }

        Map<String, String> commandDescriptions = CommandDescriptions.getCommandDescriptions();
        StringBuilder helpOutput = new StringBuilder("Available Commands:\n");

        for (Map.Entry<String, String> entry : commandDescriptions.entrySet()) {
            helpOutput.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return helpOutput.toString();
    }

    public static HelpCommand of(String[] args, Client client, SpotifyManager spotifyManager) {
        return new HelpCommand(args, client, spotifyManager);
    }
}
