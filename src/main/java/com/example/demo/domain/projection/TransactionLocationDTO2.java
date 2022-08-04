package com.example.demo.domain.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionLocationDTO2 {

   private String mti;

   private String traceNumber;

   private String merchantName;

   private String merchantStreet;
}
