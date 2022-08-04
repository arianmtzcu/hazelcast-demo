package com.example.demo.controller;

import java.util.Objects;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.projection.TransactionLocation;
import com.example.demo.domain.projection.TransactionLocationDTO;
import com.example.demo.service.DemoService;
import com.hazelcast.map.IMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1")
public class DemoController {

   private final DemoService demoService;

   @Autowired
   public DemoController(DemoService demoService) {
      this.demoService = demoService;
   }

   @GetMapping(value = "/greet")
   public ResponseEntity<String> sayHello() {
      return new ResponseEntity<>("Hello World!", HttpStatus.OK);
   }

   /*
    *  Simple Hazelcast examples & Playing with the cache!
    */
   @GetMapping(value = "/cache/uuid_map/generate/{word}")
   public ResponseEntity<String> getResult(@PathVariable("word") String word) throws InterruptedException {
      return new ResponseEntity<>(demoService.getData(word), HttpStatus.CREATED);
   }

   @GetMapping(value = "/cache/{cacheName}/find/{value}")
   public ResponseEntity<Boolean> existValueIntoCache(@PathVariable("cacheName") final String cacheName, @PathVariable("value") String value) {
      final Boolean valueIntoCache = demoService.existValueIntoCache(cacheName, value);
      return Boolean.TRUE.equals(valueIntoCache) ? new ResponseEntity<>(true, HttpStatus.OK) : new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
   }

   @GetMapping(value = "/cache/{cacheName}/find/all", produces = "application/json")
   public ResponseEntity<IMap<Object, Object>> getAllValuesIntoCache(@PathVariable("cacheName") final String cacheName) {
      final var values = demoService.getAllValuesIntoCache(cacheName);
      return !values.isEmpty() ? new ResponseEntity<>(values, HttpStatus.OK) : new ResponseEntity<>(values, HttpStatus.NO_CONTENT);
   }

   /*
    *  Spring Data & Projections examples
    */
   // using close interface projection
   @GetMapping(value = "/db/find/interface_closed", produces = "application/json")
   public ResponseEntity<Object> getTransactionLocationById() {
      return buildResponseEntity(demoService.getTransactionLocationById(2L));
   }

   // using open interface projection
   @GetMapping(value = "/db/find/interface_open", produces = "application/json")
   public ResponseEntity<Object> getTransactionFullLocationById() {
      return buildResponseEntity(demoService.getTransactionFullLocationById(4L));
   }

   // using class based
   @GetMapping(value = "/db/find/class_based", produces = "application/json")
   public ResponseEntity<Object> getPersonLocationDTO() {
      final Tuple t = demoService.getPersonLocationDTO(4L);
      return buildResponseEntity(
            new TransactionLocationDTO(t.get(0, String.class), t.get(1, String.class), t.get(2, String.class), t.get(3, String.class)));
   }

   // using class based in named query
   @GetMapping(value = "/db/find/class_based_named_query", produces = "application/json")
   public ResponseEntity<Object> getTransactionLocationDTO2() {
      return buildResponseEntity(demoService.getTransactionLocationDTO2(6L));
   }

   // using dynamically method
   @GetMapping(value = "/db/find/dynamically", produces = "application/json")
   public ResponseEntity<Object> getTransactionLocationDynamically() {
      return buildResponseEntity(demoService.getTransactionLocationDynamically(8L, TransactionLocation.class));
   }

   private ResponseEntity<Object> buildResponseEntity(final Object response) {
      return Objects.nonNull(response) ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
   }

}
