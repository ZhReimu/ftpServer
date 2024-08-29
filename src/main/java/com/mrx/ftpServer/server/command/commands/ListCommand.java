package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;

import java.io.File;
import java.net.Socket;
import java.util.Set;

/**
 * Handler for NLST (Named List) command. Lists the directory content in a short
 * format (names only)
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class ListCommand extends BaseCommand {

    @Override
    public void execute(String args) {
        Socket dataConnection = worker.getDataConnection();
        if (dataConnection == null || dataConnection.isClosed()) {
            sendMsgToClient("425 No data connection was established");
        } else {
            String[] dirContent = nlstHelper(args);
            if (dirContent == null) {
                sendMsgToClient("550 File does not exist.");
            } else {
                sendMsgToClient("125 Opening ASCII mode data connection for file list.");
                for (String s : dirContent) {
                    worker.sendDataMsgToClient(s);
                }
                sendMsgToClient("226 Transfer complete.");
                worker.closeDataConnection();
            }
        }
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("LIST", "NLST");
    }

    /**
     * A helper for the NLST command. The directory name is obtained by appending
     * "args" to the current directory
     *
     * @param args The directory to list
     * @return an array containing names of files in a directory. If the given name
     * is that of a file, then return an array containing only one element
     * (this name). If the file or directory does not exist, return nul.
     */
    private String[] nlstHelper(String args) {
        // Construct the name of the directory to list.
        String filename = Context.CURRENT_DIR.get();
        if (args != null) {
            filename = filename + Context.FILE_SEPARATOR.get() + args;
        }
        // Now get a File object, and see if the name we got exists and is a
        // directory.
        File f = new File(filename);
        if (f.exists() && f.isDirectory()) {
            return f.list();
        } else if (f.exists() && f.isFile()) {
            String[] allFiles = new String[1];
            allFiles[0] = f.getName();
            return allFiles;
        } else {
            return null;
        }
    }

}
