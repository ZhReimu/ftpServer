package com.mrx.ftpServer.server;

import com.mrx.ftpServer.server.utils.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class for an FTP server worker thread.
 *
 * @author Moritz Stueckler (SID 20414726)
 */
public class Worker implements Runnable {

    // control connection
    private final Socket controlSocket;
    private PrintWriter controlOutWriter;
    private BufferedReader controlIn;

    // data Connection
    private ServerSocket dataSocket;
    private Socket dataConnection;
    private PrintWriter dataOutWriter;

    private final int dataPort;

    private final CommandEngine commandEngine = new CommandEngine(this);

    private volatile boolean quitCommandLoop = false;

    private static final AtomicInteger workerNo = new AtomicInteger(0);

    private static final Logger logger = LoggerFactory.getLogger(Worker.class);

    /**
     * Create new worker with given client socket
     *
     * @param client   the socket for the current client
     * @param dataPort the port for the data connection
     */
    public Worker(Socket client, int dataPort) {
        this.controlSocket = client;
        this.dataPort = dataPort;
    }

    /**
     * Run method required by Java thread model
     */
    public void run() {
        Context.init();
        Thread.currentThread().setName("worker-" + workerNo.getAndIncrement());
        logger.debug("Current working directory {}", Context.CURRENT_DIR.getAsString());
        try {
            // Input from client
            controlIn = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
            // Output to client, automatically flushed after each print
            controlOutWriter = new PrintWriter(controlSocket.getOutputStream(), true);
            // Greeting
            sendMsgToClient("220 Welcome to the COMP4621 FTP-Server");
            // Get new command from client
            while (!quitCommandLoop) {
                String line = controlIn.readLine();
                // while client closed connection, we also clean worker
                if (line == null) {
                    break;
                }
                commandEngine.executeCommand(line);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            // Clean up
            try {
                Context.clear();
                controlIn.close();
                controlOutWriter.close();
                controlSocket.close();
                logger.debug("Sockets closed and worker stopped");
            } catch (IOException e) {
                logger.debug("Could not close sockets", e);
            }
        }

    }

    /**
     * Sends a message to the connected client over the control connection. Flushing
     * is automatically performed by the stream.
     *
     * @param msg The message that will be sent
     */
    public void sendMsgToClient(String msg) {
        controlOutWriter.println(msg);
    }

    /**
     * Send a message to the connected client over the data connection.
     *
     * @param msg Message to be sent
     */
    public void sendDataMsgToClient(String msg) {
        if (dataConnection == null || dataConnection.isClosed()) {
            sendMsgToClient("425 No data connection was established");
            logger.debug("Cannot send message, because no data connection is established");
        } else {
            dataOutWriter.print(msg + '\r' + '\n');
        }

    }

    /**
     * Open a new data connection socket and wait for new incoming connection from
     * client. Used for passive mode.
     *
     * @param port Port on which to listen for new incoming connection
     */
    public void openDataConnectionPassive(int port) {
        try {
            dataSocket = new ServerSocket(port);
            dataConnection = dataSocket.accept();
            dataOutWriter = new PrintWriter(dataConnection.getOutputStream(), true);
            logger.debug("Data connection - Passive Mode - established");
        } catch (IOException e) {
            logger.debug("Could not create data connection.", e);
        }

    }

    /**
     * Connect to client socket for data connection. Used for active mode.
     *
     * @param ipAddress Client IP address to connect to
     * @param port      Client port to connect to
     */
    public void openDataConnectionActive(String ipAddress, int port) {
        try {
            dataConnection = new Socket(ipAddress, port);
            dataOutWriter = new PrintWriter(dataConnection.getOutputStream(), true);
            logger.debug("Data connection - Active Mode - established");
        } catch (IOException e) {
            logger.debug("Could not connect to client data socket", e);
        }

    }

    /**
     * Close previously established data connection sockets and streams
     */
    public void closeDataConnection() {
        try {
            dataOutWriter.close();
            dataConnection.close();
            if (dataSocket != null) {
                dataSocket.close();
            }
            logger.debug("Data connection was closed");
        } catch (IOException e) {
            logger.debug("Could not close data connection", e);
        }
        dataOutWriter = null;
        dataConnection = null;
        dataSocket = null;
    }

    public Socket getDataConnection() {
        return dataConnection;
    }

    /**
     * quit command loop
     */
    public void quit() {
        quitCommandLoop = true;
    }

    public int getDataPort() {
        return dataPort;
    }
}
