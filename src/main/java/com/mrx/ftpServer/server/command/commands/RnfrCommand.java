package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;

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
        // windows explorer will only give fileName, such as test.txt, but others will give full path, such as /test/test.txt
        String currentDir = Context.CURRENT_DIR.getAsString();
        if (!args.contains(currentDir)) {
            args = currentDir + Context.FILE_SEPARATOR.get() + args;
        }
        if (!Context.getRelativeFile(args).exists()) {
            sendMsgToClient("550 file not exists");
            return;
        }
        Context.CURRENT_FILE.set(args);
        sendMsgToClient("350 Filename noted, now send RNTO");
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("RNFR");
    }
}
