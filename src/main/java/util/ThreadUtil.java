package util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zy on 17-7-23.
 */
public class ThreadUtil {
//    static {
//        PropertyConfigurator.configure("./Logger/log4j.propertites");
//    }
//    private static Logger logger = Logger.getLogger("fourthLogger");
    private static ExecutorService service = Executors.newCachedThreadPool();

    public static void Run(Runnable runnable,String message){
        service.execute(runnable);
        LoggerUtil.log.info("ThreadUtil"+service.isShutdown()+"|"+service.isTerminated()+"|"+message+"!!!!!!!!");
//        logger.info("ThreadUtil"+service.isShutdown()+"|"+service.isTerminated()+"|"+message+"!!!!!!!!");
    }
}
