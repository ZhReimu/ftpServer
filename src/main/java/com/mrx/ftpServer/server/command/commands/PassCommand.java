package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.enums.UserStatus;
import com.mrx.ftpServer.server.model.User;
import com.mrx.ftpServer.server.utils.Context;

import java.util.Set;

/**
 * Handler for PASS command. PASS receives the user password and checks if it's
 * valid.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class PassCommand extends BaseCommand {

    @Override
    public void execute(String password) {
        User user = Context.USER.get();
        // User has entered a valid username and password is correct
        UserStatus userStatus = Context.USER_STATUS.get();
        if (userStatus == UserStatus.ENTERED_USERNAME && password.equals(user.getPassword())) {
            Context.USER_STATUS.set(UserStatus.LOGGED_IN);
            sendMsgToClient("230-Welcome to HKUST");
            sendMsgToClient("230 User logged in successfully");
        } else if (userStatus == UserStatus.LOGGED_IN) {
            // User is already logged in
            sendMsgToClient("530 User already logged in");
        } else {
            // Wrong password
            sendMsgToClient("530 Not logged in");
        }
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("PASS");
    }
}
