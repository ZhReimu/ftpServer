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
    public void execute0(String args) {
        sendMsgToClient("""
                211-Extensions supported:
                 HOST
                 SIZE
                 REST STREAM
                 MDTM
                 MDTM YYYYMMDDHHMMSS[+-TZ] filename
                 MLST size*;type*;perm*;create*;modify*;
                 MFMT
                 MD5
                 XCRC "filename" start end
                 XMD5 "filename" start end
                 CLNT
                 SITE INDEX;ZONE;CMLSD;DMLSD
                 XCMLSD
                 XDMLSD
                211 END
                """);
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("FEAT");
    }
}
