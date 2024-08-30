package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;

import java.util.Set;

/**
 * Handler for NOOP (heart beat) command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class NoopCommand extends BaseCommand {

    @Override
    public void execute0(String args) {
        sendMsgToClient("200 OK");
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("NOOP");
    }
}