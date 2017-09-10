package client;

import pojo.ConfigModel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zw on 17-9-7.
 */
public class SystemContext {
    private ConfigModel model;
    private ConcurrentHashMap<String,Object> System_Ctx;
    public SystemContext(ConfigModel model) {
        this.model = model;
        this.System_Ctx = new ConcurrentHashMap();
    }

    public ConfigModel getModel() {
        return model;
    }
    public void setAttribute(String logo, Object obj){
         this.System_Ctx.put(logo,obj);
    }
    public  Object getAttribute(String logo){
        return  this.System_Ctx.get(logo);
    }
}
