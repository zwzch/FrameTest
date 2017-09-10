package impl;

import client.SystemContext;
import  java.util.*;
/**
 * Created by zw on 17-9-7.
 */
public interface DealADN extends DealHandler{

    void Deal_ADN(SystemContext ctx,List orderList);
}
