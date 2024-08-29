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
    public void execute(String file) {
        Socket dataConnection = worker.getDataConnection();
        if (file == null) {
            sendMsgToClient("501 No filename given");
        } else {
            File f = new File(Context.CURRENT_DIR.getAsString() + Context.FILE_SEPARATOR.get() + file);
            if (f.exists()) {
                sendMsgToClient("550 File already exists");
            } else {
                // Binary mode
                if (Context.TRANSFER_MODE.get() == TransferType.BINARY) {
                    BufferedOutputStream fout = null;
                    BufferedInputStream fin = null;
                    sendMsgToClient("150 Opening binary mode data connection for requested file " + f.getName());
                    try {
                        // create streams
                        fout = new BufferedOutputStream(new FileOutputStream(f));
                        fin = new BufferedInputStream(dataConnection.getInputStream());
                    } catch (Exception e) {
                        logger.debug("Could not create file streams");
                    }
                    logger.debug("Start receiving file {}", f.getName());
                    // write file with buffer
                    byte[] buf = new byte[1024];
                    int l = 0;
                    try {
                        while ((l = fin.read(buf, 0, 1024)) != -1) {
                            fout.write(buf, 0, l);
                        }
                    } catch (IOException e) {
                        logger.debug("Could not read from or write to file streams", e);
                    }
                    // close streams
                    try {
                        fin.close();
                        fout.close();
                    } catch (IOException e) {
                        logger.debug("Could not close file streams", e);
                    }
                    logger.debug("Completed receiving file {}", f.getName());
                    sendMsgToClient("226 File transfer successful. Closing data connection.");
                }
                // ASCII mode
                else {
                    sendMsgToClient("150 Opening ASCII mode data connection for requested file " + f.getName());
                    BufferedReader rin = null;
                    PrintWriter rout = null;
                    try {
                        rin = new BufferedReader(new InputStreamReader(dataConnection.getInputStream()));
                        rout = new PrintWriter(new FileOutputStream(f), true);
                    } catch (IOException e) {
                        logger.debug("Could not create file streams");
                    }
                    String s;
                    try {
                        while ((s = rin.readLine()) != null) {
                            rout.println(s);
                        }
                    } catch (IOException e) {
                        logger.debug("Could not read from or write to file streams", e);
                    }
                    try {
                        rout.close();
                        rin.close();
                    } catch (IOException e) {
                        logger.debug("Could not close file streams", e);
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
