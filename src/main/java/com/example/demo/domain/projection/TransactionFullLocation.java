package com.example.demo.domain.projection;

import org.springframework.beans.factory.annotation.Value;

public interface TransactionFullLocation {

   @Value("#{target.mti + ' ' + target.traceNumber + ' ' + target.merchantName + ' ' + target.merchantStreet}")
   String getFullLocation();

}
