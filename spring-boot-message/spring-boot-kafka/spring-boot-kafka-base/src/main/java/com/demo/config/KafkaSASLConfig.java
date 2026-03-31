package com.demo.config;

import java.util.Map;

public enum KafkaSASLConfig {
    ;

    public static void configureSASL(Map<String, Object> configProps) {
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, "wesearch", "Weseach@2025");
        configProps.put("sasl.jaas.config", jaasCfg);
    }
}
