package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;

import java.io.File;
import java.util.Set;

/**
 * Handler for RNTO (rename marked file to newName) command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class RntoCommand extends BaseCommand {

    @Override
    public void execute0(String args) {
        String oldFileName = Context.CURRENT_FILE.getAsString();
        String newFileName = Context.CURRENT_DIR.getAsString() + Context.FILE_SEPARATOR.getAsString() + args;
        if (new File(oldFileName).renameTo(new File(newFileName))) {
            sendMsgToClient("250 rename successful");
        } else {
            sendMsgToClient("553 rename failed");
        }
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("RNTO");
    }
}
