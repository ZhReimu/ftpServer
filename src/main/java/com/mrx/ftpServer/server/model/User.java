package com.mrx.ftpServer.server.model;

/**
 * @author Mr.X
 * @since 2024/8/29 20:19
 */
public class User {

    private String username;

    private String password;

    public static User of(String username, String password) {
        User user = new User();
        user.username = username;
        user.password = password;
        return user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
