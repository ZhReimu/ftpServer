package com.mrx.ftpServer.server.utils;

import com.mrx.ftpServer.server.enums.TransferType;
import com.mrx.ftpServer.server.enums.UserStatus;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.X
 * @since 2024/8/29 20:20
 */
public enum Context {

    /**
     * who am I
     */
    USER,
    /**
     * user properly logged in?
     */
    USER_STATUS,
    /**
     * current dir
     */
    CURRENT_DIR,
    /**
     * fileSeparator
     */
    FILE_SEPARATOR,
    /**
     * root dir
     */
    ROOT,
    /**
     * transfer mode
     */
    TRANSFER_MODE,
    /**
     * encoding
     */
    ENCODING,
    ;

    private static final ThreadLocal<Map<Context, Object>> context = new ThreadLocal<>();

    public static void init() {
        // Path information
        Context.FILE_SEPARATOR.set("/");
        Context.CURRENT_DIR.set("./data/test");
        Context.ROOT.set("./data");
        Context.TRANSFER_MODE.set(TransferType.ASCII);
        // user properly logged in?
        Context.USER_STATUS.set(UserStatus.NOT_LOGGED_IN);
        Context.ENCODING.set(StandardCharsets.UTF_8);
    }

    public <T> T get() {
        return Context.get(this);
    }

    public String getAsString() {
        return String.valueOf((Object) get());
    }

    public void set(Object value) {
        Context.put(this, value);
    }

    public static void put(Context key, Object value) {
        getContext().put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Context key) {
        return (T) getContext().get(key);
    }

    public static void clear() {
        context.remove();
    }

    private static Map<Context, Object> getContext() {
        Map<Context, Object> holder = context.get();
        if (holder == null) {
            holder = new ConcurrentHashMap<>();
            context.set(holder);
        }
        return holder;
    }

}
