package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;

import java.util.Set;

/**
 * Handler for the PORT command. The client issues a PORT command to the server
 * in active mode, so the server can open a data connection to the client
 * through the given address and port number.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class PortCommand extends BaseCommand {

    /**
     * @param args The first four segments (separated by comma) are the IP address.
     *             The last two segments encode the port number (port = seg1*256 +
     *             seg2)
     */
    @Override
    public void execute0(String args) {
        // Extract IP address and port number from arguments
        String[] stringSplit = args.split(",");
        String hostName = stringSplit[0] + "." + stringSplit[1] + "." + stringSplit[2] + "." + stringSplit[3];
        int p = Integer.parseInt(stringSplit[4]) * 256 + Integer.parseInt(stringSplit[5]);
        // Initiate data connection to client
        worker.openDataConnectionActive(hostName, p);
        sendMsgToClient("200 Command OK");
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("PORT");
    }
}
