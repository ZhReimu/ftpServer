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
    public void execute0(String newDir) {
        String fileSeparator = Context.FILE_SEPARATOR.get();
        File rootFile = Context.ROOT.getAsFile();
        if (newDir.endsWith(fileSeparator)) {
            newDir = newDir.substring(0, newDir.length() - 1);
        }
        if (newDir.startsWith(fileSeparator)) {
            newDir = newDir.substring(fileSeparator.length());
        }
        String filename = Context.CURRENT_DIR.get();
        logger.debug("currentDir: {}", filename);
        // fix windows explorer bug
        if (filename.equals(newDir)) {
            sendMsgToClient("250 The current directory has been changed to " + filename);
            return;
        }
        // go one level up (cd ..)
        boolean goUp = newDir.equals("..");
        if (goUp) {
            int ind = filename.lastIndexOf(fileSeparator);
            if (ind > 0) {
                filename = filename.substring(0, ind);
            }
        } else if (!newDir.equals(".")) {
            // if argument is anything else (cd . does nothing)
            if (filename.equals(fileSeparator)) {
                filename = filename + newDir;
            } else {
                filename = filename + fileSeparator + newDir;
            }
        }
        // check if file exists, is directory and is not above root directory
        File f = Context.getRelativeFile(filename);
        if (goUp) {
            f = f.getParentFile();
            filename = f.equals(rootFile) ? fileSeparator : f.getName();
        }
        boolean isSubDir = filename.length() >= rootFile.getName().length();
        boolean isRootDir = filename.equals("/");
        if (f.exists() && f.isDirectory() && (isSubDir || isRootDir)) {
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
