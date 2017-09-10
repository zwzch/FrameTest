package impl;

import client.SystemContext;


import java.util.*;
/**
 * Created by zw on 17-9-6.
 */
public class DoADN implements DealADN {
    public void Deal_ADN(SystemContext ctx, List orderList) {
        ArrayList arrayList = new ArrayList();
        for (Object obj:orderList) {
            System.out.println(obj);
            arrayList.add(obj+"++++++++");
        }
//        System.out.println("处理ADN");
//        System.out.println(msg);
        ctx.setAttribute("heihei",arrayList);
    }
//    public String DealADN(String msg) {
//        System.out.println("处理ADN");
//        System.out.println(msg);
//        return "处理ADN";
//    }
//    public String DealNOR(String msg) {
//        System.out.println(msg);
//        System.out.println("处理NOR");
//        return "处理NOR";
//    }
}
