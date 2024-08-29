package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;

import java.util.Set;

/**
 * Handler for unknown command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class DefaultCommand extends BaseCommand {

    @Override
    public void execute(String args) {
        sendMsgToClient("501 Unknown command");
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("CWD");
    }
}
