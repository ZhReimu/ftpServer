package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;

import java.io.File;
import java.util.Set;

/**
 * Handler for RMD (remove directory) command. Removes a directory.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class RmdCommand extends BaseCommand {

    /**
     * @param dir directory to be deleted.
     */
    @Override
    public void execute(String dir) {
        String filename = Context.CURRENT_DIR.get();
        // only alphanumeric folder names are allowed
        if (dir != null && dir.matches("^[a-zA-Z0-9]+$")) {
            filename = filename + Context.FILE_SEPARATOR.get() + dir;
            // check if file exists, is directory
            File d = new File(filename);
            if (d.exists() && d.isDirectory()) {
                d.delete();
                sendMsgToClient("250 Directory was successfully removed");
            } else {
                sendMsgToClient("550 Requested action not taken. File unavailable.");
            }
        } else {
            sendMsgToClient("550 Invalid file name.");
        }
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("RMD", "XRMD");
    }
}
