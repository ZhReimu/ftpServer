package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;

import java.util.Set;

/**
 * Handler for REST command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class ResetCommand extends BaseCommand {

    @Override
    public void execute0(String args) {
        sendMsgToClient("350 REST supported. Ready to resume at byte offset %s.".formatted(args));
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("REST");
    }
}
