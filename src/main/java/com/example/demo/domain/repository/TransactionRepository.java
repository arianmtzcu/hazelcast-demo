package com.example.demo.domain.repository;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.entity.Transaction;
import com.example.demo.domain.projection.TransactionFullLocation;
import com.example.demo.domain.projection.TransactionLocation;
import com.example.demo.domain.projection.TransactionLocationDTO2;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

   @Query(value = "SELECT t.MTI, t.TRACE_NUMBER as traceNumber, m.MERCHANT_NAME as merchantName, m.MERCHANT_STREET as merchantStreet "
         + "FROM TRANSACTION t INNER JOIN MERCHANT m ON t.MERCHANT_ID = m.ID  WHERE t.ID = :id", nativeQuery = true)
   TransactionLocation getTransactionLocation(@Param("id") final Long transactionId);

   @Query(value = "SELECT t.MTI, t.TRACE_NUMBER as traceNumber, m.MERCHANT_NAME as merchantName, m.MERCHANT_STREET as merchantStreet "
         + "FROM TRANSACTION t INNER JOIN MERCHANT m ON t.MERCHANT_ID = m.ID  WHERE t.ID = :id", nativeQuery = true)
   TransactionFullLocation getTransactionFullLocation(@Param("id") final Long transactionId);

   @Query(value = "SELECT t.MTI, t.TRACE_NUMBER as traceNumber, m.MERCHANT_NAME as merchantName, m.MERCHANT_STREET as merchantStreet "
         + "FROM TRANSACTION t INNER JOIN MERCHANT m ON t.MERCHANT_ID = m.ID  WHERE t.ID = :id", nativeQuery = true)
   Tuple getTransactionLocationDTO(@Param("id") final Long transactionId);

   @Query(name = "getTransactionLocationDTONamingQuery", nativeQuery = true)
   TransactionLocationDTO2 getPersonLocationDTO2(@Param("id") final Long transactionId);

   @Query(value = "SELECT t.MTI, t.TRACE_NUMBER as traceNumber, m.MERCHANT_NAME as merchantName, m.MERCHANT_STREET as merchantStreet "
         + "FROM TRANSACTION t INNER JOIN MERCHANT m ON t.MERCHANT_ID = m.ID  WHERE t.ID = :id", nativeQuery = true)
   <T> T getTransactionLocationDynamically(@Param("id") final Long transactionId, final Class<T> type);
}
