package com.example.demo.cache;

import static com.hazelcast.config.EvictionPolicy.LRU;
import static com.hazelcast.config.MaxSizePolicy.USED_HEAP_PERCENTAGE;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.IntegrityCheckerConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

@Configuration
public class HazelcastConfig {

   public static final int HZ_MAX_MEMORY_USE_PERCENTAGE = 5;

   public static final int HZ_TIME_TO_LIVE_SECONDS = 10;

   private static final String CACHE_NAME1 = "uuid_map";

   @Value("${app.hazelcast.cluster-name}")
   private String clusterName;

   @Value("${app.hazelcast.instance-name}")
   private String instanceName;

   @Value("${app.hazelcast.members}")
   private String members;

   @Bean
   public Config config() {
      return new Config()
            .setClusterName(clusterName)
            .setInstanceName(instanceName)
            .setProperty("hazelcast.rest.enabled", Boolean.TRUE.toString())
            .setProperty("hazelcast.integrity-checker.enabled", Boolean.TRUE.toString())
            .setIntegrityCheckerConfig(new IntegrityCheckerConfig().setEnabled(true))
            .setNetworkConfig(new NetworkConfig().setJoin(new JoinConfig()
                  .setMulticastConfig(new MulticastConfig().setEnabled(Boolean.FALSE))
                  .setTcpIpConfig(new TcpIpConfig().setMembers(Arrays.asList(members.split(","))).setEnabled(Boolean.TRUE))))
            .addMapConfig(new MapConfig()
                  .setName(CACHE_NAME1)
                  .setTimeToLiveSeconds(HZ_TIME_TO_LIVE_SECONDS)
                  .setEvictionConfig(new EvictionConfig()
                        .setEvictionPolicy(LRU)
                        .setMaxSizePolicy(USED_HEAP_PERCENTAGE)
                        .setSize(HZ_MAX_MEMORY_USE_PERCENTAGE)));
   }

   @Bean
   @ConditionalOnMissingBean
   public HazelcastInstance hazelcastInstance(final Config config) {
      return Hazelcast.newHazelcastInstance(config);
   }

}
