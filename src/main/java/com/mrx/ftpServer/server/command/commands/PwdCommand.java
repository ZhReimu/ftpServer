package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;

import java.util.Set;

/**
 * Handler for PWD (Print working directory) command. Returns the path of the
 * current directory back to the client.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class PwdCommand extends BaseCommand {

    @Override
    public void execute0(String args) {
        sendMsgToClient("257 \"" + Context.CURRENT_DIR.get() + "\"");
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("PWD", "XPWD");
    }
}
