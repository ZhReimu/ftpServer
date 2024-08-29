package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.enums.UserStatus;
import com.mrx.ftpServer.server.model.User;
import com.mrx.ftpServer.server.utils.Context;

import java.util.Set;

/**
 * Handler for USER command. User identifies the client.
 *
 * @author Mr.X
 * @since 2024/8/29 20:05
 */
public class UserCommand extends BaseCommand {

    @Override
    public void execute(String username) {
        // TODO: support multi user
        User user = userService.loadUsers().getFirst();
        if (username.toLowerCase().equals(user.getUsername())) {
            sendMsgToClient("331 User name okay, need password");
            Context.put(Context.USER_STATUS, UserStatus.ENTERED_USERNAME);
        } else if (Context.get(Context.USER_STATUS) == UserStatus.LOGGED_IN) {
            sendMsgToClient("530 User already logged in");
        } else {
            sendMsgToClient("530 Not logged in");
        }
    }

    @Override
    protected Set<String> getCommand() {
        return Set.of("USER");
    }
}
