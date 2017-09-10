package client;

import sun.reflect.ConstantPool;
import util.LoggerUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.StandardSocketOptions;
import java.nio.channels.SocketChannel;

/**
 * Created by zw on 17-9-4.
 */
public class ChannelOption {
    public static SocketChannel socketChannel =null;

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    //    public static final ChannelOption<Boolean> SO_KEEPALIVE = valueOf("SO_KEEPALIVE");
//    public static final ChannelOption<Integer> SO_SNDBUF = valueOf("SO_SNDBUF");
//    public static final ChannelOption<Integer> SO_RCVBUF = valueOf("SO_RCVBUF");
//    public static final ChannelOption<Boolean> SO_REUSEADDR = valueOf("SO_REUSEADDR");
//    public static final ChannelOption<Integer> SO_LINGER = valueOf("SO_LINGER");
//    public static final ChannelOption<Integer> SO_BACKLOG = valueOf("SO_BACKLOG");
//    public static final ChannelOption<Integer> SO_TIMEOUT = valueOf("SO_TIMEOUT");
//    public static final ChannelOption<Boolean> TCP_NODELAY = valueOf("TCP_NODELAY");
    public static boolean SO_KEEPALIVE( boolean flag){
        try {
             socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE,flag);
        } catch (IOException e) {
            LoggerUtil.log.debug(e);
        }
        return true;
    }
    public static boolean SO_RCVBUF(int size){
        try {
            socketChannel.setOption(StandardSocketOptions.SO_RCVBUF,size);
        } catch (IOException e) {
            LoggerUtil.log.debug(e);
        }
        return true;
    }
    public static boolean TCP_NODELAY(boolean flag){
        try {
            socketChannel.setOption(StandardSocketOptions.TCP_NODELAY,flag);
        } catch (IOException e) {
            LoggerUtil.log.debug(e);
        }
        return true;
    }

}
