package client;

import impl.DealHandler;
import pojo.ConfigModel;
import util.LoggerUtil;
import util.ThreadUtil;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * Created by zw on 17-9-4.
 */
public class NioClient {
    public static SocketChannel sc = null;
    public static Charset charset;
    public static ClientThread clientThread;
    public static ConfigModel configModel;
    public static String borginfo;
    public String charsetName;
    public static String client_Ip;
    public static int client_Port;
    private Selector selector = null;
    public NioClient(ConfigModel configModel) {
        this.configModel = configModel;
        this.charsetName= configModel.getCharser();
        this.client_Ip = configModel.getUpperIp();
        borginfo = configModel.getBorgInfo();
        this.client_Port = Integer.parseInt(configModel.getUpperPort());
        NioClient.charset = Charset.forName(this.charsetName);
    }
    public SocketChannel getChannel(){
        return  this.sc;
    }
    public boolean init()throws IOException{
        try {
            selector = Selector.open();
        } catch (IOException e) {
            LoggerUtil.log.error(e);
            return false;
        }
        charset.forName(configModel.getCharser());
        InetSocketAddress isa = new InetSocketAddress(client_Ip,client_Port);
        // 调用open静态方法创建连接到指定主机的SocketChannel
        while (sc==null){
            try {
                sc = SocketChannel.open(isa);
            }
            catch (ConnectException e){
                /*
                * Nio的重连机制
                * */
                LoggerUtil.log.error("连接请求失败,3秒后重连..."+"连接ip："+this.client_Ip+"，连接端口："+this.client_Port,e);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e1) {
                    LoggerUtil.log.error(e1.getMessage()+"连接ip:"+this.client_Ip+"，连接端口："+this.client_Port);
                    return false;
                }
            }
        }
//        sc.setOption(StandardSocketOptions.SO_RCVBUF,1024);
//        sc.setOption(StandardSocketOptions.TCP_NODELAY,true);
        // 设置该sc以非阻塞方式工作
        sc.configureBlocking(false);
        // 将SocketChannel对象注册到指定Selector
        sc.register(selector, SelectionKey.OP_READ);
        return  true;
    }
//    public boolean setHandler(DealHandler dealHandler){
//       boolean flag =  clientThread.handlerList.add(dealHandler);
//        return flag;
//    }
    public boolean addClientThread(){
        clientThread= new ClientThread(this.selector,this.configModel);
        return true;
    }
    public boolean start(ConcurrentHashMap handler_Map){
        clientThread.setHandler_map(handler_Map);
        clientThread.start();
        String line = configModel.getBorgInfo();
        // 将键盘输入的内容输出到SocketChannel中
        try {
            getChannel().write(charset.encode("LOGIN/"+line));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoggerUtil.log.debug("开始发送心跳包...");
        new Thread(new HeartBeat(sc,selector,this.configModel)).start();
        return  true;
    }
}


class HeartBeat implements Runnable{
    //发送心跳包的线程
    private ConfigModel model;
    private SocketChannel sc = null;
    private Selector selector;
    private Charset charset ;
    private String heart_Msg;
    public HeartBeat(SocketChannel sc,Selector selector, ConfigModel model) {
        this.sc = sc;
        this.selector =selector;
        this.model=model;
        this.heart_Msg = model.getHeart_Msg();
        this.charset = Charset.forName(model.getCharser());;
    }

    public void run() {
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                public void run() {
                    LoggerUtil.log.debug("-------设定要指定任务--------");
                    try {
                        LoggerUtil.log.debug("发送心跳...");
                        sc.write(charset.encode("HEARTBEAT/HEA"));
                        LoggerUtil.log.debug("发送数据成功");
                    } catch (IOException e) {
                        LoggerUtil.log.warn("心跳发送失败");
                    try {
                        reCon();
                    } catch (InterruptedException e1) {
                        LoggerUtil.log.debug("重连失败",e);
                    }
                    sc = NioClient.sc;
                }
            }
        }, 1000,1000*30);
    }
    public void reCon() throws InterruptedException {
        int Flag=0;
        try {
            InetSocketAddress isa = new InetSocketAddress(NioClient.client_Ip,NioClient.client_Port);
            LoggerUtil.log.debug("重连ip+"+NioClient.client_Ip+",重连端口"+NioClient.client_Port);
            SocketChannel newNc = null;
            while (newNc==null) {
                try {
                    Flag++;
                    if (Flag==100)
                    {
                        LoggerUtil.log.error("无法重连服务器");
                        break;}
                    selector = Selector.open();
                    newNc = SocketChannel.open(isa);
                }catch (ConnectException e){
//					System.out.println("连接请求失败，三秒后重连...");
                    LoggerUtil.log.debug("连接服务端失败，10秒后重连..."+"重连ip+"+NioClient.client_Ip+",重连端口"+NioClient.client_Port);
                    Thread.sleep(10000);
                }
            }
            if(newNc!=null){
                NioClient.sc=newNc;
                NioClient.sc.configureBlocking(false);
                // 将SocketChannel对象注册到指定Selector
                NioClient.sc.register(selector, SelectionKey.OP_READ);
                String line = NioClient.borginfo;
                // 将键盘输入的内容输出到SocketChannel中
                LoggerUtil.log.debug("重连发送"+line);
                NioClient.sc.write(charset.encode("LOGIN/"+line));
                new ClientThread(selector,NioClient.configModel).start();}
        } catch (IOException e) {
//			e.printStackTrace();
            LoggerUtil.log.debug("重连发送失败",e);
        }
    }
}