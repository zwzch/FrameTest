package impl;

import client.SingleLink;
import client.SystemContext;

import java.nio.charset.Charset;

/**
 * Created by zw on 17-9-7.
 */
public class DoNOR implements DealNOR{
    public void Deal_NOR(SystemContext ctx, String msg) {
        System.out.println(msg);
       Charset charset =  Charset.forName(ctx.getModel().getCharser());
        SingleLink link = new SingleLink();
        link.init("127.0.0.1",9999).setCharset( Charset.forName(ctx.getModel().getCharser())).setModel(ctx.getModel()).send("fafafa".getBytes(),new byte["fafafa".length()],"error");
        link.write(charset.encode(String.valueOf(link.getRecv_bytes())));
    }
}
