package com.example.demo.domain.entity;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.example.demo.domain.projection.TransactionLocationDTO2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedNativeQuery(name = "getTransactionLocationDTONamingQuery", query =
      "SELECT t.MTI, t.TRACE_NUMBER as traceNumber, m.MERCHANT_NAME as merchantName, m.MERCHANT_STREET as merchantStreet "
            + "FROM TRANSACTION t INNER JOIN MERCHANT m ON t.MERCHANT_ID = m.ID  WHERE t.ID = :id", resultSetMapping =
      "TransactionLocationDTO2Mapping")
@SqlResultSetMapping(name = "TransactionLocationDTO2Mapping", classes = @ConstructorResult(targetClass = TransactionLocationDTO2.class, columns = {
      @ColumnResult(name = "mti", type = String.class), @ColumnResult(name = "traceNumber", type = String.class),
      @ColumnResult(name = "merchantName", type = String.class), @ColumnResult(name = "merchantStreet", type = String.class) }))
public class Transaction {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   private String mti;

   private String processingCode;

   private String traceNumber;

   private long merchantId;

   public static boolean supports(final Class<?> clazz) {
      return Transaction.class == clazz;
   }
}
