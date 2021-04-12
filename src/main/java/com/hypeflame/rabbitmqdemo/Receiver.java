package com.hypeflame.rabbitmqdemo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class Receiver {

  private AtomicInteger received = new AtomicInteger();
  private final RestTemplate restTemplate;

  public Boolean receiveMessage(byte[] bytes) {
    String body = new String(bytes);
    int i = received.incrementAndGet();
    System.out.println("Received <" + i + "> Body: " + body);

    try {
      restTemplate.postForEntity("/v1/processed", body, String.class);
      return Boolean.TRUE;
    } catch (Exception ex) {
      log.error(ex.getMessage());
      return Boolean.FALSE;
    }

  }

  public Integer receivedMessages() {
    return received.get();
  }

  public void clear() {
    received.set(0);
  }

}