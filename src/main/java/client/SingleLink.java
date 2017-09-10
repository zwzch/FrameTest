package client;

import pojo.ConfigModel;
import util.LoggerUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.charset.Charset;

/**
 * Created by zw on 17-9-9.
 */
public  class SingleLink {
    private Charset charset;
    private ConfigModel model;
    private SocketTmp socketTmp;
    private byte[] recv_bytes;
    public Charset getCharset() {
        return charset;
    }
    public SingleLink setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }
    public ConfigModel getModel() {
        return model;
    }
    public SingleLink setModel(ConfigModel model) {
        this.model = model;
        return this;
    }
    public byte[] getRecv_bytes() {
        return recv_bytes;
    }
    public SingleLink init(String ip, int port){
        this.socketTmp = new SocketTmp(ip,port);
        return this;
    }
    public SingleLink send( byte[] send_bytes,byte [] get_bytes,String error_msg){
        try {
            this.socketTmp.getSocket().send_Recv(send_bytes,get_bytes);
        } catch (Exception e) {
            LoggerUtil.log.error(e);
            LoggerUtil.log.debug("单个连接请求错误");
            NioClient.clientThread.key.interestOps(SelectionKey.OP_READ);
            try {
                LoggerUtil.log.debug(error_msg);
                NioClient.sc.write(charset.encode(error_msg));
            } catch (IOException e1) {
//                e1.printStackTrace();
                LoggerUtil.log.error(e);
            }
        }
        this.recv_bytes=get_bytes;
// String order = ScaleUtil.HextoS(bytes);
//            String order = new String(bytes);
//          System.out.println("recive:"+order);
//        tmp.close();
        return  this;
    }
    public SingleLink close(){
        this.socketTmp.close();
        return this;
    }
    public void write(ByteBuffer btf){
        boolean flag;
        try {
           NioClient.sc.write(btf);
        } catch (IOException e) {
//            e.printStackTrace();
            LoggerUtil.log.error("长连接返回请求失败",e);
            LoggerUtil.log.debug("注册SelectionKey.OP_READ事件为下一次读取作准备");
            ClientThread.key.interestOps(SelectionKey.OP_READ);
        }

    }
}
