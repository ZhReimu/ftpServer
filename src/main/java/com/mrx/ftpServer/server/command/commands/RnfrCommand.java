package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;

import java.io.File;
import java.util.Set;

/**
 * Handler for RNFR (mark a file for rename) command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class RnfrCommand extends BaseCommand {

    @Override
    public void execute0(String args) {
        String fileName = Context.CURRENT_DIR.getAsString() + Context.FILE_SEPARATOR.getAsString() + args;
        if (!new File(fileName).exists()) {
            sendMsgToClient("550 file not exists");
            return;
        }
        Context.CURRENT_FILE.set(fileName);
        sendMsgToClient("350 Filename noted, now send RNTO");
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("RNFR");
    }
}
