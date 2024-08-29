package com.mrx.ftpServer;

import com.mrx.ftpServer.server.Server;

/**
 * @author Mr.X
 * @since 2024/8/29 19:11
 */
public class Main {

    public static void main(String[] args) {
        new Server("127.0.0.1", 1025);
    }

}
