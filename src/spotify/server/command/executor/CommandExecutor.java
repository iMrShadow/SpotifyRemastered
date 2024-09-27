package spotify.server.command.executor;

import spotify.server.command.Command;

public class CommandExecutor {

    public String execute(Command cmd) throws Exception {
        if (cmd == null) {
            return "Invalid command.";
        }

        return cmd.call();
    }
}
