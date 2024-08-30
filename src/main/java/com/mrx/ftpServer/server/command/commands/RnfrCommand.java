package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;

import java.util.Set;

/**
 * Handler for RNFR (rename files) command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class RnfrCommand extends BaseCommand {

    @Override
    public void execute0(String args) {
        // TODO: complete it
        sendMsgToClient("200 file successful renamed to " + args);
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("RNFR");
    }
}
