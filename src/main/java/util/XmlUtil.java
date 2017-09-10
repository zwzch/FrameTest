package util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import pojo.ConfigModel;

import javax.print.Doc;
import javax.print.attribute.Attribute;
import java.io.File;
import java.util.Iterator;
import java.util.*;

/**
 * Created by zw on 17-9-3.
 */
public class XmlUtil {
    public Document document = null;
    public Document load(String path){
        // 创建saxReader对象
        SAXReader reader = new SAXReader();
        // 通过read方法读取一个文件 转换成Document对象
        try {
            if (document==null){
             document = reader.read(path);}
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }
    public ConfigModel parseClient(Document document){
        //获取根节点元素对象
        Element rootElementnode = document.getRootElement();
        ConfigModel model = new ConfigModel();
        Element upperNet = rootElementnode.element("upperNet");
        model.setUpperIp(upperNet.elementText("upper_ip"));
        model.setUpperPort(upperNet.elementText("upper_port"));
        model.setCharser(rootElementnode.element("Charset").elementText("upperCharsetName"));
        Element httpNet = rootElementnode.element("httpNet");
        model.setHttpIp(httpNet.elementText("http_ip"));
        model.setHttpPort(httpNet.elementText("http_port"));
        model.setBorgInfo(rootElementnode.element("borg").elementText("borg_info"));
        Element adn = rootElementnode.element("ADN");
        model.setAdn_size(Integer.parseInt(adn.elementText("adn_size")));
        model.setPoll_miles(Long.parseLong(adn.elementText("polling_character")));
        model.setHeart_Gap(Long.parseLong(rootElementnode.element("HEART_BEAT").elementText("heart_gap")));
        model.setHeart_Msg(rootElementnode.element("HEART_BEAT").elementText("heart_Msg"));
        return model;
    }
    public void listNodes(Element node) {
        System.out.println("当前节点的名称：：" + node.getName());
        // 获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        // 遍历属性节点
        for (Attribute attr : list) {
            System.out.println(attr.toString() + "-----" + attr.getName()
                    + "---" + attr.toString());
        }

        if (!(node.getTextTrim().equals(""))) {
            System.out.println("文本内容：：：：" + node.getText());
        }

        // 当前节点下面子节点迭代器
        Iterator<Element> it = node.elementIterator();
        // 遍历
        while (it.hasNext()) {
            // 获取某个子节点对象
            Element e = it.next();
            // 对子节点进行遍历
            listNodes(e);
        }
    }
    public static void main(String[] args) {
        String realPath = XmlUtil.class.getClassLoader().getResource("")
                .getFile();
        System.out.println(realPath);
        System.out.println(System.getProperty("user.dir"));
        System.out.println(MyPath.getPath());
    }
}
