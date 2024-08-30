package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.enums.TransferType;
import com.mrx.ftpServer.server.utils.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Set;

/**
 * Handler for STOR (Store) command. Store receives a file from the client and
 * saves it to the ftp server.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class StorCommand extends BaseCommand {

    private static final Logger logger = LoggerFactory.getLogger(StorCommand.class);

    /**
     * @param file The file that the user wants to store on the server
     */
    @Override
    public void execute0(String file) {
        Socket dataConnection = worker.getDataConnection();
        if (file == null) {
            sendMsgToClient("501 No filename given");
        } else {
            String fileName = Context.CURRENT_DIR.getAsString() + Context.FILE_SEPARATOR.get() + file;
            File f = Context.getRelativeFile(fileName);
            if (f.exists()) {
                sendMsgToClient("550 File already exists");
            } else {
                // Binary mode
                if (Context.TRANSFER_MODE.get() == TransferType.BINARY) {
                    sendMsgToClient("150 Opening binary mode data connection for requested file " + f.getName());
                    try (BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(f));
                         BufferedInputStream fin = new BufferedInputStream(dataConnection.getInputStream())
                    ) {
                        logger.debug("Start receiving file {}", f.getName());
                        // write file with buffer
                        byte[] buf = new byte[1024];
                        int l;
                        while ((l = fin.read(buf, 0, 1024)) != -1) {
                            fout.write(buf, 0, l);
                        }
                    } catch (Exception e) {
                        logger.error("file transmission failed", e);
                    }
                    logger.debug("Completed receiving file {}", f.getName());
                    sendMsgToClient("226 File transfer successful. Closing data connection.");
                } else {
                    // ASCII mode
                    sendMsgToClient("150 Opening ASCII mode data connection for requested file " + f.getName());
                    try (BufferedReader rin = new BufferedReader(new InputStreamReader(dataConnection.getInputStream()));
                         PrintWriter rout = new PrintWriter(new FileOutputStream(f), true)
                    ) {
                        String s;
                        while ((s = rin.readLine()) != null) {
                            rout.println(s);
                        }
                    } catch (IOException e) {
                        logger.debug("Could not create file streams");
                    }
                    sendMsgToClient("226 File transfer successful. Closing data connection.");
                }
            }
            worker.closeDataConnection();
        }
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("STOR");
    }
}
