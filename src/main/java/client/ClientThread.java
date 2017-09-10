package client;

import impl.DealADN;
import impl.DealEND;
import impl.DealHandler;
import impl.DealNOR;
import pojo.ConfigModel;
import util.LoggerUtil;
import util.ThreadUtil;
import util.UpperConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zw on 17-9-4.
 */
class ClientThread extends Thread {
    public Selector selector;
    public String upperStr="";
    public boolean Flag=false;
    public ConfigModel configModel;
    public ConcurrentHashMap handler_map;
    public static SystemContext context;
    public ClientThread(Selector ss,ConfigModel configModel){

        this.selector=ss;
        this.configModel = configModel;
        context = new SystemContext(configModel);
    }

    public void setHandler_map(ConcurrentHashMap handler_map) {
        this.handler_map = handler_map;
    }

    public ArrayList orderList = new ArrayList();
    public static SelectionKey key = null;
    public void run() {
        try {
            while (selector.select() > 0) {
                // 遍历每个有可用IO操作Channel对应的SelectionKey
                for (SelectionKey sk : selector.selectedKeys()) {
                    key = sk;
                    // 删除正在处理的SelectionKey
                    selector.selectedKeys().remove(sk);
                    // 如果该SelectionKey对应的Channel中有可读的数据
                    if (sk.isReadable()) {
                        // 使用NIO读取Channel中的数据
                        SocketChannel sc = (SocketChannel) sk.channel();
                        ByteBuffer buff = ByteBuffer.allocate(1024);
                        String content = "";
                        int a = 0;
                        while ((a = sc.read(buff)) > 0) {
//							NClient.log.debug("读取数据成功");
                            sc.read(buff);
                            buff.flip();
                            content += NioClient.charset.decode(buff);
                        }
//                        System.out.println(content);
                        String judgement =null;
                        try {
                           judgement= content.substring(0, 3);
                        }catch (Exception e){
                            LoggerUtil.log.error(e);
                        }
                        if (!judgement.equals(UpperConfig.HEA) && Flag == false&&judgement!=null) {
                            String[] temp = content.split("&");
                            content = upperStr + temp[0];
                            orderList.add(content);
                            if (temp.length != 1) {
                                upperStr = temp[temp.length - 1];
                                if (temp.length > 2) {
                                    for (int i = 1; i <= temp.length - 2; i++) {
                                        orderList.add(temp[i]);
                                    }
                                }
                            }
                            LoggerUtil.log.debug(configModel.getAdn_size());
//                            System.out.println(configModel.getAdn_size());
                            if(orderList.size()==configModel.getAdn_size()){
                                Flag=true;
                                System.out.println(upperStr);
                                try{
                                    judgement=upperStr.substring(0,3);}catch (Exception e){
                                    LoggerUtil.log.debug("数据无粘包");
                                }
                                LoggerUtil.log.debug("读取adn完毕,judgement:"+judgement);
                            }
//							new  controlParse().ParseorderList(content);
//							log.info("向checkOrder类的check方法传递参数"+content+"执行check方法");
//							System.out.println(content);
                            LoggerUtil.log.debug("注册SelectionKey.OP_READ事件为下一次读取作准备");
//						    if(content!=null&&!content.equals("")){
//						    new checkOrder(content).check();}
                            sk.interestOps(SelectionKey.OP_READ);
                        }
                        if(Flag==true&&content!=null&&!content.equals("")){
//                            String normalJudge = content.substring(0, 2);
//                            if (normalJudge.equals("##")) {
//                                LoggerUtil.log.debug("开始解析正常指令" + content);
////                                new checkOrder(content).check();
//                            }
                            switch (judgement){
                                case DEAL_SELECT.END:
                                    DealADN dealADN = (DealADN) this.handler_map.get(DEAL_SELECT.ADN);
                                    dealADN.Deal_ADN(context,orderList);
                                    DealEND dealEND = (DealEND) this.handler_map.get(DEAL_SELECT.END);
                                    dealEND.Deal_END(context,"END");
                                    ThreadUtil.Run(dealEND.Do_Loop(context),"Deal_Loop");
                                    break;
                                case DEAL_SELECT.NOR:
                                    DealNOR dealNOR = (DealNOR) this.handler_map.get(DEAL_SELECT.NOR);
                                    dealNOR.Deal_NOR(context,content);
                                    break;
                                case DEAL_SELECT.HEA:
                                    LoggerUtil.log.debug("心跳回值"+content);
                                    break;
                                default:
                                    LoggerUtil.log.info("指令传输错误"+content);
                            }
//                            if (judgement.equals("END")) {
////                                NClient.log.debug("进行序列化");
////                                for (Object obj : orderList) {
//                                    DealADN dealADN = (DealADN) this.handler_map.get(DEAL_SELECT.ADN);
//                                    dealADN.Deal_ADN(context,orderList);
//                                    DealEND dealEND = (DealEND) this.handler_map.get(DEAL_SELECT.END);
//                                    dealEND.Deal_END(context,"END");
//                                    dealEND.Do_Loop(context);
//                            }else if(judgement.equals("")){
//                                DealNOR dealNOR = (DealNOR) this.handler_map.get(DEAL_SELECT.NOR);
//                                dealNOR.Deal_NOR(context,content);
//                            }
                        }
                    }
                }
            }} catch (IOException e){
            LoggerUtil.log.debug(e);
            LoggerUtil.log.debug("注册SelectionKey.OP_READ事件为下一次读取作准备");
            key.interestOps(SelectionKey.OP_READ);
        }
    }
}