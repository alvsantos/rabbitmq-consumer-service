package com.hypeflame.rabbitmqdemo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/consumed")
public class Api {

    private final Receiver receiver;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Integer getConsumed() {
        return receiver.receivedMessages();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        receiver.clear();
    }

}
