package com.zz.bms.util.redis.pool;

import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author Administrator
 */
public final class SetSentinel {


    private static Set<String> SET_NODES = new HashSet<String>();

    private static Logger log = Logger.getLogger(SetSentinel.class);

    static {
        Properties properties = new Properties();
        log.info("-----开始加载redis配置文件-----");
        try {
            properties.load(SetSentinel.class.getClassLoader().getResourceAsStream("redisSentinel.properties"));
            String nodesString = properties.getProperty("nodes");
            for (String node :
                    nodesString.split(",")) {
                SET_NODES.add(node);
            }
            log.info("-----redis配置文件加载成功-----");
        } catch (Exception ex) {
            log.error("reids配置文件加载失败", ex);
        }
    }

    public static Set<String> getSentinelNodes() {
        return  SET_NODES;
    }

}
