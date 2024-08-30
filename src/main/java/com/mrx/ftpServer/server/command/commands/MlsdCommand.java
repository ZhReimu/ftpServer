package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.utils.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

/**
 * Handler for MLSD command.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class MlsdCommand extends ListCommand {

    private static final String template = "size=%s;type=%s;perm=%s;create=%s;modify=%s; %s";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    protected Set<String> getCommand() {
        return Set.of("MLSD");
    }

    @Override
    protected String[] nlstHelper(String args) {
        String currentDir = Context.CURRENT_DIR.getAsString();
        String fileSeparator = Context.FILE_SEPARATOR.getAsString();
        return Arrays.stream(super.nlstHelper(args)).map(it -> Context.getRelativeFile(currentDir + fileSeparator + it))
                .map(this::format).toArray(String[]::new);
    }

    private String format(File it) {
        try {
            BasicFileAttributes attr = Files.readAttributes(it.toPath(), BasicFileAttributes.class);
            long size = attr.size();
            Type type = Type.valueOf(it);
            // TODO: complete it
            String perm = "r";
            String creteTime = formatTime(attr.creationTime().toMillis());
            String lastModifiedTime = formatTime(attr.lastModifiedTime().toMillis());
            return template.formatted(size, type, perm, creteTime, lastModifiedTime, it.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String formatTime(long mills) {
        LocalDateTime time = LocalDateTime.ofInstant(new Date(mills).toInstant(), ZoneOffset.ofHours(8));
        return formatter.format(time);
    }

    private enum Type {
        /**
         * child dir
         */
        cdir,
        /**
         * parent dir
         */
        pdir,
        /**
         * file
         */
        file;

        public static Type valueOf(File f) {
            // TODO: show parent dir
            return f.isDirectory() ? cdir : file;
        }
    }

}
