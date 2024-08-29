package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;

import java.util.Set;

/**
 * Handler for the QUIT command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class QuitCommand extends BaseCommand {

    @Override
    public void execute0(String password) {
        sendMsgToClient("221 Closing connection");
        worker.quit();
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("QUIT");
    }
}
