package com.demo.config;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DeleteConsumerGroupsResult;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

@Slf4j
@Configuration
public class AdminDestroyBean {

    @Value("${my.kafka.bootstrap-servers}")
    private String servers;

    public static final String TEST_GROUP = "demo-group-test";

    /**
     * 优雅删除指定的消费组
     * 本地使用IndexController#shutdown()模拟
     */
    @PreDestroy
    public void deleteConsumeGroup() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        // 显式覆盖配置参数
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        // 配置SASL
        KafkaSASLConfig.configureSASL(configProps);
        AdminClient adminClient = AdminClient.create(configProps);
        deleteGroup(adminClient);
    }

    private void deleteGroup(AdminClient adminClient) {
        try {
            DeleteConsumerGroupsResult result = adminClient.deleteConsumerGroups(Collections.singletonList(TEST_GROUP));
            // 等待删除完成
            result.all().get();
            log.warn("Consumer group '" + TEST_GROUP + "' has been deleted.");
        } catch (Exception ex) {
            log.error("Failed to delete consumer group: " + ex);
        } finally {
            // 关闭 AdminClient
            adminClient.close();
        }
    }
}
