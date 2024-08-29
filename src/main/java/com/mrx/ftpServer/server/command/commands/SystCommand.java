package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;

import java.util.Set;

/**
 * Handler for SYST command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class SystCommand extends BaseCommand {

    @Override
    public void execute(String args) {
        sendMsgToClient("215 COMP4621 FTP Server Homebrew");
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("SYST");
    }
}
