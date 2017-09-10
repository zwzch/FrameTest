package util;
import org.apache.log4j.*;

/**
 * Created by zw on 17-9-3.
 */
public class LoggerUtil {
    public static Logger log;
    static {
        PropertyConfigurator.configure("/home/zw/demo/FrameTest/src/main/resources/log4j.properties");
        log=Logger.getLogger(LoggerUtil.class.getName());
    }
public static void main(String[] args) {
    log.debug("heihei");
}
}
