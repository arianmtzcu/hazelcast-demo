package com.example.demo.domain.projection;

import lombok.Value;

@Value
public class TransactionLocationDTO {

   private String mti;

   private String traceNumber;

   private String merchantName;

   private String merchantStreet;
}
