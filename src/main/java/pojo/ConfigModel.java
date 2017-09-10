package pojo;

/**
 * Created by zw on 17-9-4.
 */
public class ConfigModel {
    private String upperIp;
    private String upperPort;
    private String charser;
    private String httpIp;
    private String httpPort;
    private String borgInfo;
    private int adn_size;
    private String heart_Msg;
    private long poll_miles;
    private  long Heart_Gap;

    public String getHeart_Msg() {
        return heart_Msg;
    }

    public void setHeart_Msg(String heart_Msg) {
        this.heart_Msg = heart_Msg;
    }

    public long getHeart_Gap() {
        return Heart_Gap;
    }

    public void setHeart_Gap(long heart_Gap) {
        Heart_Gap = heart_Gap;
    }


    public int getAdn_size() {
        return adn_size;
    }

    public void setAdn_size(int adn_size) {
        this.adn_size = adn_size;
    }

    public long getPoll_miles() {
        return poll_miles;
    }

    public void setPoll_miles(long poll_miles) {
        this.poll_miles = poll_miles;
    }

    public String getBorgInfo() {
        return borgInfo;
    }
    public void setBorgInfo(String borgInfo) {
        this.borgInfo = borgInfo;
    }
    public String getHttpIp() {
        return httpIp;
    }
    public void setHttpIp(String httpIp) {
        this.httpIp = httpIp;
    }
    public String getHttpPort() {
        return httpPort;
    }
    public void setHttpPort(String httpPort) {
        this.httpPort = httpPort;
    }
    public String getCharser() {
        return charser;
    }
    public void setCharser(String charser) {
        this.charser = charser;
    }
    public String getUpperIp() {
        return upperIp;
    }
    public void setUpperIp(String upperIp) {
        this.upperIp = upperIp;
    }
    public String getUpperPort() {
        return upperPort;
    }
    public void setUpperPort(String upperPort) {
        this.upperPort = upperPort;
    }

    @Override
    public String toString() {
        return "ConfigModel{" +
                "upperIp='" + upperIp + '\'' +
                ", upperPort='" + upperPort + '\'' +
                ", charser='" + charser + '\'' +
                ", httpIp='" + httpIp + '\'' +
                ", httpPort='" + httpPort + '\'' +
                ", borgInfo='" + borgInfo + '\'' +
                ", adn_size=" + adn_size +
                ", poll_miles=" + poll_miles +
                '}';
    }
}
