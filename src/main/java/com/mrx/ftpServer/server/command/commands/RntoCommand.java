package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;

import java.util.Set;

/**
 * Handler for RNTO (rename marked file to newName) command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class RntoCommand extends BaseCommand {

    @Override
    public void execute0(String newFileName) {
        // windows explorer will only give fileName, such as test.txt, but others will give full path, such as /test/test.txt
        String currentDir = Context.CURRENT_DIR.getAsString();
        if (!newFileName.contains(currentDir)) {
            newFileName = currentDir + Context.FILE_SEPARATOR.get() + newFileName;
        }
        String oldFileName = Context.CURRENT_FILE.getAsString();
        if (Context.getRelativeFile(oldFileName).renameTo(Context.getRelativeFile(newFileName))) {
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
