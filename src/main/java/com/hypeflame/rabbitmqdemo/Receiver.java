package com.hypeflame.rabbitmqdemo;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Receiver {

  private AtomicInteger received = new AtomicInteger();

  public void receiveMessage(byte[] bytes) {
    String base64String = new String(bytes);
    int i = received.incrementAndGet();
    System.out.println("Received <" + i + "> Body: " + base64String);
  }

  public Integer receivedMessages() {
    return received.get();
  }

}