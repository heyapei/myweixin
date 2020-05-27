package com.hyp.myweixin.config.logback;
 
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
 
/**
 * @author xiaoming
 * @date 2019/5/14 11:37
 * @description
 */
public class LogPortConfig extends ClassicConverter {
    private static final Logger logger = LoggerFactory.getLogger(LogPortConfig.class);
    private static String webPort;
 
    static {
        try {
            List<MBeanServer> serverList = MBeanServerFactory.findMBeanServer(null);
            for (MBeanServer server : serverList) {
                Set<ObjectName> names = new HashSet<ObjectName>();
                names.addAll(server.queryNames(new ObjectName("Catalina:type=Connector,*"), null));
                Iterator<ObjectName> it = names.iterator();
                while (it.hasNext()) {
                    ObjectName oName = (ObjectName) it.next();
                    String pValue = (String) server.getAttribute(oName, "protocol");
                    if (StringUtils.equals("HTTP/1.1", pValue)) {
                        webPort = ObjectUtils.toString(server.getAttribute(oName, "port"));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取port失败,影响logback的文件拼接", e);
            webPort = null;
        }
    }
 
    @Override
    public String convert(ILoggingEvent event) {
        return webPort;
    }
}