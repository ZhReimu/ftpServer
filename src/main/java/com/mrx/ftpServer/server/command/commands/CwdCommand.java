package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;

import java.io.File;
import java.util.Set;

/**
 * Handler for CWD (change working directory) command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class CwdCommand extends BaseCommand {

    @Override
    public void execute0(String args) {
        String filename = Context.CURRENT_DIR.get();
        // fix windows explorer bug
        if (filename.equals(args)) {
            sendMsgToClient("250 The current directory has been changed to " + filename);
            return;
        }
        Context fileSeparator = Context.FILE_SEPARATOR;
        // go one level up (cd ..)
        if (args.equals("..")) {
            int ind = filename.lastIndexOf(fileSeparator.get());
            if (ind > 0) {
                filename = filename.substring(0, ind);
            }
        }
        // if argument is anything else (cd . does nothing)
        else if (!args.equals(".")) {
            filename = filename + fileSeparator.get() + args;
        }
        // check if file exists, is directory and is not above root directory
        File f = new File(filename);
        if (f.exists() && f.isDirectory() && (filename.length() >= Context.ROOT.getAsString().length())) {
            Context.CURRENT_DIR.set(filename);
            sendMsgToClient("250 The current directory has been changed to " + filename);
        } else {
            sendMsgToClient("550 Requested action not taken. File unavailable.");
        }
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("CWD");
    }
}
