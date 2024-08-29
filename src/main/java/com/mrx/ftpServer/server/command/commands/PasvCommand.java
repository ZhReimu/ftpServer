package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;

import java.util.Set;

/**
 * Handler for PASV command which initiates the passive mode. In passive mode
 * the client initiates the data connection to the server. In active mode the
 * server initiates the data connection to the client.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class PasvCommand extends BaseCommand {

    @Override
    public void execute0(String args) {
        // Using fixed IP for connections on the same machine
        // For usage on separate hosts, we'd need to get the local IP address from
        // somewhere
        // Java sockets did not offer a good method for this
        String myIp = "127.0.0.1";
        String[] myIpSplit = myIp.split("\\.");
        int dataPort = worker.getDataPort();
        int p1 = dataPort / 256;
        int p2 = dataPort % 256;
        sendMsgToClient(String.format("227 Entering Passive Mode (%s,%s,%s,%s,%s,%s)", myIpSplit[0], myIpSplit[1], myIpSplit[2], myIpSplit[3], p1, p2));
        worker.openDataConnectionPassive(dataPort);
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("PASV");
    }
}
