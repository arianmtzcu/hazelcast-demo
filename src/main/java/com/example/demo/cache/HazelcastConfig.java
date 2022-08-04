package com.example.demo.cache;

import static com.example.demo.util.Constants.CACHE_NAME1;
import static com.example.demo.util.Constants.CACHE_NAME2;
import static com.example.demo.util.Constants.CACHE_NAME3;
import static com.example.demo.util.Constants.HZ_MAX_MEMORY_USE_PERCENTAGE;
import static com.example.demo.util.Constants.INT_30;
import static com.example.demo.util.Constants.INT_3660;
import static com.example.demo.util.Constants.INT_60;
import static com.hazelcast.config.EvictionPolicy.LRU;
import static com.hazelcast.config.MaxSizePolicy.USED_HEAP_PERCENTAGE;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.domain.entity.Merchant;
import com.example.demo.domain.entity.serializer.MerchantStreamSerializer;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.IntegrityCheckerConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

@Configuration
public class HazelcastConfig {

   @Value("${app.hazelcast.cluster-name}")
   private String clusterName;

   @Value("${app.hazelcast.instance-name}")
   private String instanceName;

   @Value("${app.hazelcast.members}")
   private String members;

   @Bean
   public Config config() {
      final Config config = new Config()
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
                  .setTimeToLiveSeconds(INT_30)
                  .setEvictionConfig(
                        new EvictionConfig().setEvictionPolicy(LRU).setMaxSizePolicy(USED_HEAP_PERCENTAGE).setSize(HZ_MAX_MEMORY_USE_PERCENTAGE)))
            .addMapConfig(new MapConfig()
                  .setName(CACHE_NAME2)
                  .setTimeToLiveSeconds(INT_3660)
                  .setEvictionConfig(
                        new EvictionConfig().setEvictionPolicy(LRU).setMaxSizePolicy(USED_HEAP_PERCENTAGE).setSize(HZ_MAX_MEMORY_USE_PERCENTAGE)))
            .addMapConfig(new MapConfig()
                  .setName(CACHE_NAME3)
                  .setTimeToLiveSeconds(INT_60)
                  .setEvictionConfig(
                        new EvictionConfig().setEvictionPolicy(LRU).setMaxSizePolicy(USED_HEAP_PERCENTAGE).setSize(HZ_MAX_MEMORY_USE_PERCENTAGE)));

      config.getSerializationConfig()
            .addSerializerConfig(new SerializerConfig().setImplementation(new MerchantStreamSerializer()).setTypeClass(Merchant.class));

      return config;
   }

   @Bean
   @ConditionalOnMissingBean
   public HazelcastInstance hazelcastInstance(final Config config) {
      return Hazelcast.newHazelcastInstance(config);
   }

}
