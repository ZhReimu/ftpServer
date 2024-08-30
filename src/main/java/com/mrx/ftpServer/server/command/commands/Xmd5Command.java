package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

/**
 * Handler for XMD5 (md5 calc) command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class Xmd5Command extends BaseCommand {

    @Override
    public void execute0(String fileName) {
        File file = Context.getRelativeFile(fileName);
        try {
            sendMsgToClient("250 " + DigestUtils.md5Hex(new FileInputStream(file)));
        } catch (IOException e) {
            logger.error("md5 failed", e);
            sendMsgToClient("550 md5 file '%s' failed.".formatted(fileName));
        }
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("XMD5");
    }
}
