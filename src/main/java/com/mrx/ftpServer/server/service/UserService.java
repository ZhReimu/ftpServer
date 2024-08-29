package com.mrx.ftpServer.server.service;

import com.mrx.ftpServer.server.model.User;

import java.util.List;

/**
 * @author Mr.X
 * @since 2024/8/29 20:18
 */
public class UserService {

    public List<User> loadUsers() {
        User u1 = User.of("comp4621", "network");
        User u2 = User.of("root", "root");
        return List.of(u2, u1);
    }

}
