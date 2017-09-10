package client;

import util.LoggerUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by zw on 17-9-7.
 */
public class SocketTmp {

    private Socket socket;
    private  String upper_ip;
    private  int upper_port;
    private OutputStream outputStream=null;
    private InputStream inputStream=null;
    public SocketTmp(String upper_ip, int port) {
//        this.model = model;
        this.upper_ip = upper_ip;
        this.upper_port = port;
        try {
            socket=new Socket(upper_ip,upper_port);
            outputStream=this.socket.getOutputStream();
            inputStream=this.socket.getInputStream();
        } catch (IOException e) {
            LoggerUtil.log.error(e);
        }

    }
    public SocketTmp getSocket() {
        return  this;
    }
    public SocketTmp setSoTimeout(int miles){
        try {
            this.socket.setSoTimeout(miles);
        } catch (SocketException e) {
            LoggerUtil.log.error("设置参数错误 miles"+miles);
        }
        return  this;
    }
    public boolean close(){
        try {
            this.outputStream.close();
            this.inputStream.close();
            this.socket.close();
        } catch (IOException e) {
//            e.printStackTrace();
            LoggerUtil.log.error(e);
        }
       return true;
    }
    public byte[] send_Recv(byte[] send_msg, byte[] recv_msg) throws IOException{
            outputStream.write(send_msg);
            outputStream.flush();
            LoggerUtil.log.debug("----------->");
            byte[] bytes = recv_msg;
            inputStream.read(bytes);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
               LoggerUtil.log.error(e);
            }
            if(bytes!=null)
                return bytes;
            return null;
    }
}
