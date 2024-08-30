package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;

import java.util.Set;

/**
 * Handler for RESET command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class ResetCommand extends BaseCommand {

    @Override
    public void execute0(String args) {
        sendMsgToClient("200 OK.");
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("RESET");
    }
}
