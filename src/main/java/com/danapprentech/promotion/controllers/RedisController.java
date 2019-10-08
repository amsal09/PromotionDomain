package com.danapprentech.promotion.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

@RestController
@RequestMapping(value = "redis")
public class RedisController {
    @GetMapping(value = "/redis")
    public ResponseEntity<String> redis() {
        Jedis jedis = new Jedis();
        jedis.set("test","1");
        jedis.expire("test",5);
        return new ResponseEntity<> (jedis.get("test"), HttpStatus.OK);
    }

    @GetMapping(value = "/redis/detail")
    public ResponseEntity<String> redisDetail() {
        Jedis jedis = new Jedis();

        return new ResponseEntity<> (jedis.get("test"), HttpStatus.OK);
    }
}
