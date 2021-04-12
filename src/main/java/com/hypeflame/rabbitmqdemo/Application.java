package com.hypeflame.rabbitmqdemo;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareBatchMessageListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
public class Application {

  static final String topicExchangeName = "spring-boot-exchange";

  static final String queueName = "spring-boot";

  static final Boolean autoAck = Boolean.FALSE;
  static final Boolean errorNack = Boolean.FALSE;
  static final Integer ttl = 600000;

  @Bean
  Queue queue() {
    Queue queue = new Queue(queueName, false);
    queue.addArgument("x-message-ttl", ttl);
    return queue;
  }

  @Bean
  TopicExchange exchange() {
    return new TopicExchange(topicExchangeName);
  }

  @Bean
  Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
  }

  @Bean
  public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
                                                           Receiver receiver) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setAcknowledgeMode(autoAck ? AcknowledgeMode.AUTO : AcknowledgeMode.MANUAL);
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName);
    container.setMessageListener(exampleListener(receiver));
    return container;
  }

  public ChannelAwareBatchMessageListener exampleListener(Receiver receiver) {
    return new ChannelAwareBatchMessageListener() {
      @Override
      public void onMessage(Message message, Channel channel) throws Exception {
        if (receiver.receiveMessage(message.getBody())) {
          if (Boolean.FALSE.equals(autoAck)) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
          }
        } else {
          if (Boolean.FALSE.equals(autoAck) && Boolean.FALSE.equals(errorNack)) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
          }
        }
      }

      @Override
      public void onMessageBatch(List<Message> messages, Channel channel) {

      }

    };
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
            .rootUri("http://localhost:8081")
            .build();
  }

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(Application.class, args);
  }

}