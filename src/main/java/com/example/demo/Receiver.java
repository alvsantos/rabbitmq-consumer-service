package com.example.demo;

import java.util.concurrent.CountDownLatch;

import com.rabbitmq.client.MessageProperties;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  public static int NUMBER_OF_MESSAGES = 1000;

  private CountDownLatch latch = new CountDownLatch(NUMBER_OF_MESSAGES);

  public void receiveMessage(String message) {
    System.out.println("Received <" + message + "> Remaining <" + (latch.getCount() - 1) + ">");
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}