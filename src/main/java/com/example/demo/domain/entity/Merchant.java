package com.example.demo.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Merchant {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   private String merchantName;

   private String merchantStreet;

   private String merchantCity;

   public static boolean supports(final Class<?> clazz) {
      return Merchant.class == clazz;
   }
}
