
package redisson.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redisson.codec.FastJsonCodec;
import redisson.codec.ProtoStuffCodec;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.Kryo5Codec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonConfig {

    private Cluster cluster;

    private String host;

    private String port;

    private String password;

    @Data
    public static class Cluster {

        private List<String> nodes;

        /**
         * Maximum number of redirects to follow when executing commands across the cluster.
         */
        private Integer maxRedirects;
    }

    /**
     * RedissonClient bean
     */
    @Primary
    @Bean(name = "redissonClient", destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        return getRedissonClient(new JsonJacksonCodec());
    }

    @Bean(name = "kryoRedisson", destroyMethod = "shutdown")
    public RedissonClient kryoRedisson() {
        return getRedissonClient(new Kryo5Codec());
    }

    @Bean(name = "protoStuffRedisson", destroyMethod = "shutdown")
    public RedissonClient protoStuffRedisson() {
        return getRedissonClient(new ProtoStuffCodec());
    }

    @Bean(name = "fastJsonRedisson", destroyMethod = "shutdown")
    public RedissonClient fastJsonRedisson() {
        return getRedissonClient(new FastJsonCodec());
    }

    /**
     * 根据 codec 创建 RedissonClient
     *
     * @param codec codec
     * @return RedissonClient
     */
    public RedissonClient getRedissonClient(Codec codec) {
        Config redissonConfig = new Config();
        redissonConfig.setCodec(codec);
        // 设置'看门狗'续命时间, 默认30秒
        redissonConfig.setLockWatchdogTimeout(30 * 1000);

        if (null != cluster && !CollectionUtils.isEmpty(cluster.getNodes())) {
            List<String> nodes = cluster.getNodes();
            String[] clusterNodes = new String[nodes.size()];
            for (int i = 0; i < nodes.size(); i++) {
                clusterNodes[i] = "redis://" + nodes.get(i);
            }
            // 集群模式
            ClusterServersConfig clusterServersConfig = redissonConfig.useClusterServers();
            clusterServersConfig.addNodeAddress(clusterNodes)
                    .setPassword(password)
                    .setSlaveConnectionMinimumIdleSize(24)
                    .setMasterConnectionMinimumIdleSize(24);

        } else {
            // 单点模式
            SingleServerConfig singleServerConfig = redissonConfig.useSingleServer();
            singleServerConfig.setAddress("redis://" + host + ":" + port)
                    .setPassword(password);
        }
        return Redisson.create(redissonConfig);
    }
}
