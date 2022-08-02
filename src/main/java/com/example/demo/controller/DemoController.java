package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DemoService;
import com.hazelcast.map.IMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/cache")
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

   @GetMapping(value = "/generate/{word}")
   public ResponseEntity<String> getResult(@PathVariable("word") String word) throws InterruptedException {
      return new ResponseEntity<>(demoService.getData(word), HttpStatus.CREATED);
   }

   @GetMapping(value = "/find/{uuid}")
   public ResponseEntity<Boolean> existValueIntoCache(@PathVariable("uuid") String uuid) {
      return demoService.existValueIntoCache(uuid) ?
            new ResponseEntity<>(true, HttpStatus.OK) :
            new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
   }

   @GetMapping(value = "/find/all", produces = "application/json")
   public ResponseEntity<IMap<Object, Object>> getAllValuesIntoCache() {
      final var values = demoService.getAllValuesIntoCache();
      return !values.isEmpty() ?
            new ResponseEntity<>(values, HttpStatus.OK) :
            new ResponseEntity<>(values, HttpStatus.NO_CONTENT);
   }

}
