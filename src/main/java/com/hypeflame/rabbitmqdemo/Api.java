package com.hypeflame.rabbitmqdemo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Api {

    private final Receiver receiver;

    @GetMapping("v1/consumed")
    public Integer getConsumed() {
        return receiver.receivedMessages();
    }

}
