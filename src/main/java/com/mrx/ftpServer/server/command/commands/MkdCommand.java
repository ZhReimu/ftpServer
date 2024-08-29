package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Set;

/**
 * Handler for the MKD (make directory) command. Creates a new directory on the
 * server.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class MkdCommand extends BaseCommand {

    private static final Logger logger = LoggerFactory.getLogger(MkdCommand.class);

    /**
     * @param args Directory name
     */
    @Override
    public void execute(String args) {
        // Allow only alphanumeric characters
        if (args != null && args.matches("^[a-zA-Z0-9]+$")) {
            File dir = new File(Context.CURRENT_DIR.getAsString() + Context.FILE_SEPARATOR.get() + args);
            if (!dir.mkdir()) {
                sendMsgToClient("550 Failed to create new directory");
                logger.debug("Failed to create new directory");
            } else {
                sendMsgToClient("250 Directory successfully created");
            }
        } else {
            sendMsgToClient("550 Invalid name");
        }
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("MKD", "XMKD");
    }
}
