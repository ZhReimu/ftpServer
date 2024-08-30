package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;

import java.io.File;
import java.util.Set;

/**
 * Handler for DELE (delete) command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class DeleCommand extends BaseCommand {

    @Override
    public void execute0(String fileName) {
        File file = Context.getRelativeFile(fileName);
        if (!file.exists()) {
            sendMsgToClient("550 '%s': no such file or directory.".formatted(fileName));
            return;
        }
        if (file.delete()) {
            sendMsgToClient("250 File '%s' deleted.".formatted(fileName));
            return;
        }
        sendMsgToClient("550 delete file '%s' failed.".formatted(fileName));
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("DELE");
    }
}
