package com.mrx.ftpServer.server.command;

import com.mrx.ftpServer.server.Worker;
import com.mrx.ftpServer.server.service.UserService;
import com.mrx.ftpServer.server.utils.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

/**
 * @author Mr.X
 * @since 2024/8/29 20:03
 */
public abstract class BaseCommand {

    protected Worker worker;

    protected UserService userService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public boolean support(String command) {
        return getCommand().stream().anyMatch(it -> it.equalsIgnoreCase(command));
    }

    public void execute(String args) {
        String command = Context.CURRENT_COMMAND.getAsString();
        logger.debug("executing command: {}, args: {}", command, args);
        execute0(args);
        logger.debug("execute command: {} completed", command);
    }

    protected abstract void execute0(String args);

    /**
     * which supported command
     *
     * @return commandName
     */
    protected abstract Set<String> getCommand();

    /**
     * Sends a message to the connected client over the control connection. Flushing
     * is automatically performed by the stream.
     *
     * @param msg The message that will be sent
     */
    protected void sendMsgToClient(String msg) {
        worker.sendMsgToClient(msg);
    }

    public BaseCommand setWorker(Worker worker) {
        this.worker = worker;
        return this;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseCommand that = (BaseCommand) o;
        return Objects.equals(getCommand(), that.getCommand());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCommand());
    }

    @Override
    public String toString() {
        return getCommand().toString();
    }
}
