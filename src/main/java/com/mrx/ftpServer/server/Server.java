package com.mrx.ftpServer.server;

import com.mrx.ftpServer.server.utils.TracerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A very simple FTP Server class. On receiving a new connection it creates a
 * new worker thread.
 *
 * @author Moritz Stueckler
 */
public class Server {

    private ServerSocket welcomeSocket;

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public Server(String hostName, int controlPort) {
        startServer(hostName, controlPort);
    }

    private void startServer(String hostName, int controlPort) {
        try {
            welcomeSocket = new ServerSocket(controlPort, 0, new InetSocketAddress(hostName, controlPort).getAddress());
        } catch (IOException e) {
            logger.error("Could not create server socket", e);
            System.exit(-1);
        }
        logger.info("FTP Server started listening on port {}", controlPort);
        int noOfThreads = 0;
        while (true) {
            try {
                TracerUtils.startTracer();
                Socket client = welcomeSocket.accept();
                // Port for incoming dataConnection (for passive mode) is the controlPort +
                // number of created threads + 1
                int dataPort = controlPort + noOfThreads++ + 1;
                // Create new worker thread for new connection
                Thread w = TracerUtils.startTracer(new Worker(client, dataPort));
                logger.info("New connection received. Worker was created.");
                w.start();
            } catch (IOException e) {
                logger.warn("Exception encountered on accept", e);
            } finally {
                TracerUtils.stopTracer();
            }
        }
//        try {
//            welcomeSocket.close();
//            logger.info("Server was stopped");
//        } catch (IOException e) {
//            logger.warn("Problem stopping server", e);
//            System.exit(-1);
//        }
    }

}
