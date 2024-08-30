package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;
import org.apache.commons.codec.digest.PureJavaCrc32;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

/**
 * Handler for XCRC (crc32 calc) command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class XcrcCommand extends BaseCommand {

    @Override
    public void execute0(String fileName) {
        File file = Context.getRelativeFile(fileName);
        try {
            PureJavaCrc32 crc32 = new PureJavaCrc32();
            crc32.update(Files.readAllBytes(file.toPath()));
            sendMsgToClient("250 " + crc32.getValue());
        } catch (IOException e) {
            logger.error("md5 failed", e);
            sendMsgToClient("550 crc file '%s' failed.".formatted(fileName));
        }
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("XCRC");
    }
}
