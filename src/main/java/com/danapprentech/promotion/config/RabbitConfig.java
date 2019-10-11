package com.danapprentech.promotion.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue() {
        return new Queue ("queue.payment");
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange (
                "data.exchange");
    }

    @Bean
    public Binding binding(DirectExchange exchange, Queue queue) {
        return BindingBuilder.bind (queue).to (exchange).with ("");
    }
}
