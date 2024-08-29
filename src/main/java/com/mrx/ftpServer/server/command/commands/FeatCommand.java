package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;

import java.util.Set;

/**
 * Handler for the FEAT (features) command. Feat transmits the
 * abilities/features of the server to the client. Needed for some ftp clients.
 * This is just a dummy message to satisfy clients, no real feature information
 * included.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class FeatCommand extends BaseCommand {

    @Override
    public void execute(String args) {
        sendMsgToClient("211-Extensions supported:");
        sendMsgToClient("211 END");
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("CWD");
    }
}
