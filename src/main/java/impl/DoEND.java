package impl;

import client.HttpClientTmp;
import client.SocketTmp;
import client.SystemContext;

import java.io.IOException;
import java.util.*;
/**
 * Created by zw on 17-9-7.
 */
public class DoEND implements DealEND {


    public void Deal_END(SystemContext ctx, String msg) {
        ArrayList arrayList = (ArrayList) ctx.getAttribute("heihei");
        for (Object obj:arrayList) {
            System.out.println(obj.toString());
        }
    }

    public Runnable Do_Loop(SystemContext context) {
        return new Runnable() {
            @Override
            public void run() {
                while (true){
                    SocketTmp tmp = new SocketTmp("127.0.0.1",33333);
                    byte[] bytes = new byte["fafafa".length()];
                    int i;
                    for ( i=0;i<3;i++){
                        try {
                            tmp.getSocket().send_Recv("fafafa".getBytes(),bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//        String order = ScaleUtil.HextoS(bytes);
                        String order = new String(bytes);
                        System.out.println("recive:"+order);
//                new HttpClientTmp().Http_Send("127.0.0.1",9999,"heihei");
                    }
                    tmp.close();
                    try {
                        Thread.sleep(1000*60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


    }
}
