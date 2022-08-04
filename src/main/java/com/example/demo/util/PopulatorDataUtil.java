package com.example.demo.util;

import static com.example.demo.util.Constants.CACHE_NAME2;
import static com.example.demo.util.Constants.CACHE_NAME3;
import static com.example.demo.util.Constants.INT_3600;
import static com.example.demo.util.Constants.INT_60;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.domain.entity.Merchant;
import com.example.demo.domain.entity.Transaction;
import com.example.demo.domain.repository.MerchantRepository;
import com.example.demo.domain.repository.TransactionRepository;
import com.hazelcast.core.HazelcastInstance;

@Component
public class PopulatorDataUtil implements InitializingBean {

   private static final Logger LOGGER = LogManager.getLogger(PopulatorDataUtil.class);

   private final TransactionRepository personRepository;

   private final MerchantRepository merchantRepository;

   private final HazelcastInstance hazelcastInstance;

   @Autowired
   public PopulatorDataUtil(TransactionRepository personRepository, MerchantRepository merchantRepository, HazelcastInstance hazelcastInstance) {
      super();
      this.personRepository = personRepository;
      this.merchantRepository = merchantRepository;
      this.hazelcastInstance = hazelcastInstance;
   }

   @Override
   public void afterPropertiesSet() {
      LOGGER.info("Populate Process :: Starting...");
      populateDataBase();
      populateCache();
      LOGGER.info("Populate Process :: Finished successful!");
   }

   private void populateDataBase() {
      LOGGER.info("PP Step#1 :: Inserting test data into the database...");
      for (int i = 1; i <= 10; i++) {
         Merchant merchant = this.merchantRepository.save(getRandomMerchant(i));
         Transaction transaction = getRandomTransaction(i);
         transaction.setMerchantId(merchant.getId());
         this.personRepository.save(transaction);
      }
   }

   private void populateCache() {
      LOGGER.info("PP Step#2 :: Inserting test data temporary in the cache obtained from the database...");
      final List<Transaction> allTransactions = personRepository.findAll();
      final List<Merchant> allMerchants = merchantRepository.findAll();
      fillCacheMap(CACHE_NAME2, allTransactions, Transaction.class);
      LOGGER.info("PP Step#2.1 :: Inserted test data into cache map \"{}\".", CACHE_NAME2);
      fillCacheMap(CACHE_NAME3, allMerchants, Merchant.class);
      LOGGER.info("PP Step#2.2 :: Inserted test data into cache map \"{}\".", CACHE_NAME3);
   }

   private Transaction getRandomTransaction(int i) {
      Transaction txn = new Transaction();
      txn.setMti(String.valueOf((200 + i)));
      txn.setProcessingCode("pc100" + i);
      txn.setTraceNumber("tn-000" + i);
      return txn;
   }

   private Merchant getRandomMerchant(int i) {
      Merchant merchant = new Merchant();
      merchant.setMerchantName("Name-" + i);
      merchant.setMerchantStreet("Street-" + i);
      merchant.setMerchantCity("City-" + i);
      return merchant;
   }

   private void fillCacheMap(final String cacheName, final List<?> list, final Class<?> clazz) {
      for (Object o : list) {

         if (Transaction.supports(clazz)) {
            Transaction txn = (Transaction) o;
            hazelcastInstance.getMap(cacheName).put(txn.getId(), txn.getTraceNumber(), INT_3600, TimeUnit.SECONDS);

         } else if (Merchant.supports(clazz)) {
            Merchant mch = (Merchant) o;
            hazelcastInstance.getMap(cacheName).put(mch.getId(), mch, INT_60, TimeUnit.SECONDS);
         }

      }
   }

}
