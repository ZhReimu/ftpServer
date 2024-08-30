package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Handler for unknown command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class DefaultCommand extends BaseCommand {

    private static final Logger logger = LoggerFactory.getLogger(DefaultCommand.class);

    @Override
    public void execute0(String args) {
        logger.warn("unknown command: {} {}", Context.CURRENT_COMMAND.get(), args);
        sendMsgToClient("501 Unknown command");
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("CWD");
    }
}
