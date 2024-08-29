package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.enums.TransferType;
import com.mrx.ftpServer.server.utils.Context;

import java.util.Set;

/**
 * Handler for the TYPE command. The type command sets the transfer mode to
 * either binary or ascii mode
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class TypeCommand extends BaseCommand {

    /**
     * @param mode Transfer mode: "a" for Ascii. "i" for image/binary.s
     */
    @Override
    public void execute0(String mode) {
        if (mode.equalsIgnoreCase("A")) {
            Context.TRANSFER_MODE.set(TransferType.ASCII);
            sendMsgToClient("200 OK");
        } else if (mode.equalsIgnoreCase("I")) {
            Context.TRANSFER_MODE.set(TransferType.BINARY);
            sendMsgToClient("200 OK");
        } else {
            sendMsgToClient("504 Not OK");
        }
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("TYPE");
    }
}
