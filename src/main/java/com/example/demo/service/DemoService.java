package com.example.demo.service;

import static com.example.demo.util.Constants.CACHE_NAME1;

import java.util.UUID;

import javax.persistence.Tuple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.demo.domain.projection.TransactionFullLocation;
import com.example.demo.domain.projection.TransactionLocation;
import com.example.demo.domain.projection.TransactionLocationDTO2;
import com.example.demo.domain.repository.MerchantRepository;
import com.example.demo.domain.repository.TransactionRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

@Service
@CacheConfig(cacheNames = "uuid_map")
public class DemoService {

   private static final Logger LOGGER = LogManager.getLogger(DemoService.class);

   private final HazelcastInstance hazelcastInstance;

   private final TransactionRepository transactionRepository;

   private final MerchantRepository merchantRepository;

   @Autowired
   public DemoService(HazelcastInstance hazelcastInstance, TransactionRepository transactionRepository, MerchantRepository merchantRepository) {
      this.hazelcastInstance = hazelcastInstance;
      this.transactionRepository = transactionRepository;
      this.merchantRepository = merchantRepository;
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

   public Boolean existValueIntoCache(final String cacheName, final String value) {
      return hazelcastInstance.getMap(cacheName).containsValue(value);
   }

   public IMap<Object, Object> getAllValuesIntoCache(final String cacheName) {
      return hazelcastInstance.getMap(cacheName);
   }

   public TransactionLocation getTransactionLocationById(final Long transactionId) {
      return transactionRepository.getTransactionLocation(transactionId);
   }

   public TransactionFullLocation getTransactionFullLocationById(final Long transactionId) {
      return transactionRepository.getTransactionFullLocation(transactionId);
   }

   public Tuple getPersonLocationDTO(final Long transactionId) {
      return this.transactionRepository.getTransactionLocationDTO(transactionId);
   }

   public TransactionLocationDTO2 getTransactionLocationDTO2(final Long transactionId) {
      return this.transactionRepository.getPersonLocationDTO2(transactionId);
   }

   public TransactionLocation getTransactionLocationDynamically(final long transactionId, final Class<TransactionLocation> transactionLocationClass) {
      return this.transactionRepository.getTransactionLocationDynamically(transactionId, TransactionLocation.class);
   }
}

