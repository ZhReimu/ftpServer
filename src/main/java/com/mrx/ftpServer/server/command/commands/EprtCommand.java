package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;

import java.util.Set;

/**
 * Handler for the EPORT command. The client issues an EPORT command to the
 * server in active mode, so the server can open a data connection to the client
 * through the given address and port number.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class EprtCommand extends BaseCommand {

    /**
     * @param args This string is separated by vertical bars and encodes the IP
     *             version, the IP address and the port number
     */
    @Override
    public void execute0(String args) {
        final String IPV4 = "1";
        final String IPV6 = "2";
        // Example arg: |2|::1|58770| or |1|132.235.1.2|6275|
        String[] splitArgs = args.split("\\|");
        String ipVersion = splitArgs[1];
        String ipAddress = splitArgs[2];
        if (!IPV4.equals(ipVersion) && !IPV6.equals(ipVersion)) {
            throw new IllegalArgumentException("Unsupported IP version");
        }
        int port = Integer.parseInt(splitArgs[3]);
        // Initiate data connection to client
        worker.openDataConnectionActive(ipAddress, port);
        sendMsgToClient("200 Command OK");
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("EPRT");
    }
}
