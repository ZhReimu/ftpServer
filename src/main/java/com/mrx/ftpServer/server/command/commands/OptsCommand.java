package com.mrx.ftpServer.server.command.commands;

import com.mrx.ftpServer.server.command.BaseCommand;
import com.mrx.ftpServer.server.utils.Context;

import java.nio.charset.Charset;
import java.util.Set;

/**
 * handle encode change
 *
 * @author Mr.X
 * @since 2024/8/30 07:27
 */
public class OptsCommand extends BaseCommand {

    @Override
    public void execute(String args) {
        String[] split = args.split(" ");
        String encode = split[0];
        String operate = split[1];
        if (operate.equalsIgnoreCase("on")) {
            Context.ENCODING.set(Charset.forName(encode));
        }
        worker.sendMsgToClient("200 OPTS %s command successful - %s encoding now %s".formatted(encode, encode, operate));
    }

    /**
     * which supported command
     *
     * @return commandName
     */
    @Override
    protected Set<String> getCommand() {
        return Set.of("OPTS");
    }
}
