import client.ChannelOption;
import client.DEAL_SELECT;
import client.NioClient;
import impl.DealHandler;
import impl.DoADN;
import impl.DoEND;
import impl.DoNOR;

import org.dom4j.Document;
import pojo.ConfigModel;
import util.LoggerUtil;
import util.XmlUtil;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zw on 17-9-3.
 */
public class BootStrap {
    private ConcurrentHashMap Handler_MAP;
    private String configPath;
    private   NioClient nioClient;
    private ChannelOption channelOption;
    public BootStrap() {
    }
    public BootStrap(String configPath)
    {
        this.configPath = configPath;
        this.Handler_MAP = new ConcurrentHashMap();
    }
         public BootStrap open() {
        XmlUtil xmlUtil = new XmlUtil();
        Document document = xmlUtil.load(this.configPath);
        ConfigModel model = xmlUtil.parseClient(document);
        nioClient = new NioClient(model);
        try {
            nioClient.init();
        } catch (IOException e) {
            LoggerUtil.log.error(e);
        }
        channelOption= new ChannelOption();
        channelOption.setSocketChannel(nioClient.getChannel());
        System.out.println(model);
        return  this;
    }
   public BootStrap option(boolean flag){
//       ChannelOption.TCP_NODELAY(nioClient.getChannel(),true);
       return this;
   }

   public BootStrap setThread(){
       nioClient.addClientThread();
       return  this;
   }
   public BootStrap addHandler(String logo,DealHandler dealHandler){
       this.Handler_MAP.put(logo,dealHandler);
//       nioClient.setHandler(dealHandler);
       return this;
   }
   public boolean start(){
       nioClient.start(this.Handler_MAP);
       return true;
   }
    public static void main(String[] args) {
        BootStrap bootStrap = new BootStrap("/home/zw/demo/FrameTest/src/main/resources/Configation.xml");
        bootStrap .open()
                  .option(ChannelOption.SO_KEEPALIVE(true))
                  .option(ChannelOption.SO_RCVBUF(1024))
                  .setThread()
                  .addHandler(DEAL_SELECT.ADN,new DoADN())
                  .addHandler(DEAL_SELECT.END,new DoEND())
                  .addHandler(DEAL_SELECT.NOR,new DoNOR())
                  .start();
    }
}
