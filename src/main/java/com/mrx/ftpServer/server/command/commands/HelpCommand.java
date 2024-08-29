package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;

import java.util.Set;

/**
 * Handler for HELP command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class HelpCommand extends BaseCommand {

    @Override
    public void execute0(String args) {
        sendMsgToClient("Welcome to FTP Server");
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("HELP");
    }
}
