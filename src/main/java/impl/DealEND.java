package impl;

import client.SystemContext;

import java.util.Vector;

/**
 * Created by zw on 17-9-7.
 */
public interface DealEND extends DealHandler{
    void Deal_END(SystemContext ctx, String msg);
    Runnable Do_Loop(SystemContext context);
}
