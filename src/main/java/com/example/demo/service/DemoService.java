package com.example.demo.service;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

@Service
@CacheConfig(cacheNames = "uuid_map")
public class DemoService {

   public static final String UUID_MAP = "uuid_map";

   private static final Logger LOGGER = LogManager.getLogger(DemoService.class);

   private final HazelcastInstance hazelcastInstance;

   @Autowired
   public DemoService(HazelcastInstance hazelcastInstance) {
      this.hazelcastInstance = hazelcastInstance;
   }

   @Cacheable(value = "uuid_map", key = "#key")
   public String getData(final String key) throws InterruptedException {
      LOGGER.info("Running sleep action :: 2.5 seconds");
      Thread.sleep(2500);
      return generateUUID(key).toString();
   }

   private UUID generateUUID(final String key) {
      final UUID uuid = UUID.randomUUID();
      LOGGER.info(String.format("Saving value action :: key=%s --> uuid=%s", key, uuid));
      return uuid;
   }

   public Boolean existValueIntoCache(final String uuid) {
      return hazelcastInstance.getMap(UUID_MAP).containsValue(uuid);
   }

   public IMap<Object, Object> getAllValuesIntoCache() {
      return hazelcastInstance.getMap(UUID_MAP);
   }
}
