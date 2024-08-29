package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;

import java.util.Set;

/**
 * Handler for EPSV command which initiates extended passive mode. Similar to
 * PASV but for newer clients (IPv6 support is possible but not implemented
 * here).
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class EpsvCommand extends BaseCommand {

    @Override
    public void execute(String args) {
        int dataPort = worker.getDataPort();
        sendMsgToClient("229 Entering Extended Passive Mode (|||" + dataPort + "|)");
        worker.openDataConnectionPassive(dataPort);
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("EPSV");
    }
}
