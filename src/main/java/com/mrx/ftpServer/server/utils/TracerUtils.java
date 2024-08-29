package com.mrx.ftpServer.server.utils;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.MDC;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mr.X
 * @since 2023-12-01 下午 9:01:38
 */
public class TracerUtils {

    public static final String TRACER_POOL = "tracerPool";

    private static final String TRACE_ID = "traceId";

    private static final AtomicInteger sequence = new AtomicInteger(0);

    /**
     * 为当前线程 MDC 设置一个 TraceId
     */
    public static void startTracer() {
        startTracer(getTraceId());
    }

    /**
     * 为当前线程 MDC 设置一个 TraceId
     */
    public static void startTracer(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    /**
     * 移除当前线程 MDC 中的 traceId
     */
    public static void stopTracer() {
        MDC.remove(TRACE_ID);
    }

    public static Thread startTracer(Runnable r) {
        return new Thread(decorateTask(r));
    }

    public static Thread startTracer(Thread thread) {
        Runnable r = decorateTask(thread);
        Thread targetThread = new Thread(thread.getThreadGroup(), r, thread.getName());
        targetThread.setDaemon(thread.isDaemon());
        targetThread.setPriority(thread.getPriority());
        return targetThread;
    }

    /**
     * 装饰一个 runnable, 为其线程 MDC 中设置一个来自父线程的 traceId 并在执行结束后将其移除<br/>
     * <b>本方法并不会移除 traceId, 需要手动处理</b>
     *
     * @param r runnable
     * @return runnable
     */
    public static Runnable decorateTask(Runnable r) {
        // 获取当线程上下文的 traceId
        String traceId = getTraceId();
        return () -> {
            // 将其设置到子线程中
            startTracer(traceId);
            r.run();
        };
    }

    /**
     * 获取当前线程 MDC 中的 traceId, 如果没有则会生成一个新的
     *
     * @return traceId
     */
    public static String getTraceId() {
        return Optional.ofNullable(MDC.get(TRACE_ID)).orElseGet(TracerUtils::newTraceId);
    }

    /**
     * 获取一个新的 traceId
     *
     * @return traceId
     */
    private static String newTraceId() {
        return sofaTraceId();
    }

    private static String sofaTraceId() {
        sequence.compareAndSet(9000, 1000);
        String ip = findFirstNonLoopbackAddress().map(InetAddress::getAddress).map(Hex::encodeHexString)
                .orElse(Hex.encodeHexString(InetAddress.getLoopbackAddress().getAddress()));
        long time = System.currentTimeMillis();
        long pid = ProcessHandle.current().pid();
        return ip + time + sequence.getAndIncrement() + pid;
    }

    private static Optional<InetAddress> findFirstNonLoopbackAddress() {
        try {
            return NetworkInterface.networkInterfaces()
                    .filter(TracerUtils::notLoopBack)
                    .flatMap(NetworkInterface::inetAddresses)
                    .filter(Inet4Address.class::isInstance)
                    .findFirst();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean notLoopBack(NetworkInterface networkInterface) {
        try {
            return !networkInterface.isLoopback();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

}
