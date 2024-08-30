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
 * Handler for the RETR (retrieve) command. Retrieve transfers a file from the
 * ftp server to the client.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class RetrCommand extends BaseCommand {

    private static final Logger logger = LoggerFactory.getLogger(RetrCommand.class);

    /**
     * @param file The file to transfer to the user
     */
    @Override
    public void execute0(String file) {
        String currDirectory = Context.CURRENT_DIR.get();
        String fileSeparator = Context.FILE_SEPARATOR.get();
        Socket dataConnection = worker.getDataConnection();
        File f = Context.getRelativeFile(currDirectory + fileSeparator + file);
        if (!f.exists()) {
            sendMsgToClient("550 File does not exist");
        } else {
            // Binary mode
            if (Context.TRANSFER_MODE.get() == TransferType.BINARY) {
                BufferedOutputStream fout = null;
                BufferedInputStream fin = null;
                sendMsgToClient("150 Opening binary mode data connection for requested file " + f.getName());
                try {
                    // create streams
                    fout = new BufferedOutputStream(dataConnection.getOutputStream());
                    fin = new BufferedInputStream(new FileInputStream(f));
                } catch (Exception e) {
                    logger.debug("Could not create file streams");
                }
                logger.debug("Starting file transmission of {}", f.getName());
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
                logger.debug("Completed file transmission of {}", f.getName());
                sendMsgToClient("226 File transfer successful. Closing data connection.");
            }

            // ASCII mode
            else {
                sendMsgToClient("150 Opening ASCII mode data connection for requested file " + f.getName());
                BufferedReader rin = null;
                PrintWriter rout = null;
                try {
                    rin = new BufferedReader(new FileReader(f));
                    rout = new PrintWriter(dataConnection.getOutputStream(), true);
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

    @Override
    protected Set<String> getCommand() {
        return Set.of("RETR");
    }
}
