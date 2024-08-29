package com.mrx.ftpServer.server;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.command.commands.DefaultCommand;
import com.mrx.ftpServer.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Mr.X
 * @since 2024/8/29 20:03
 */
public class CommandEngine {

    private final List<BaseCommand> commands;

    private final DefaultCommand defaultCommand;

    private static final UserService userService = new UserService();

    private static final Logger logger = LoggerFactory.getLogger(CommandEngine.class);

    public CommandEngine(Worker worker) {
        this.commands = ServiceLoader.load(BaseCommand.class).stream().map(ServiceLoader.Provider::get)
                .peek(it -> it.setWorker(worker).setUserService(userService))
                .toList();
        defaultCommand = commands.stream().filter(it -> it.getClass().isAssignableFrom(DefaultCommand.class))
                .map(DefaultCommand.class::cast).findFirst().orElseThrow();
    }

    /**
     * Main command dispatcher method. Separates the command from the arguments and
     * dispatches it to single handler functions.
     *
     * @param c the raw input from the socket consisting of command and arguments
     */
    public void executeCommand(String c) {
        // split command and arguments
        int index = c.indexOf(' ');
        String command = ((index == -1) ? c.toUpperCase() : (c.substring(0, index)).toUpperCase());
        String args = ((index == -1) ? null : c.substring(index + 1));
        logger.debug("Command: {} Args: {}", command, args);
        // dispatcher mechanism for different commands
        BaseCommand cmd = commands.stream().filter(it -> it.support(command)).findFirst().orElse(defaultCommand);
        logger.debug("dispatched to {}", cmd);
        cmd.execute(args);
    }

}
