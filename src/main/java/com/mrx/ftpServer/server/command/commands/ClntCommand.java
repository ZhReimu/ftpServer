package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;

import java.util.Set;

/**
 * Handler for CLNT (client) command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class ClntCommand extends BaseCommand {

    @Override
    public void execute0(String args) {
        Context.CURRENT_CLIENT.set(args);
        sendMsgToClient("200 Noted OK.");
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("CLNT");
    }
}
