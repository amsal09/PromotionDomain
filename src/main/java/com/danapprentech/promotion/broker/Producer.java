package com.danapprentech.promotion.broker;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class Producer {
    private String queue;

    private RabbitTemplate template = createRabbitTemplate();

    private DirectExchange exchange = exchange();

    public Producer(String queue) {
        this.queue = queue;
    }

    public void sendToExchange(String message) {
        template.convertAndSend(queue, message);
    }

    private  RabbitTemplate createRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(getConnectionFactory());
        rabbitTemplate.setExchange(queue);
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

    private ConnectionFactory getConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setCloseTimeout(100000);
        return connectionFactory;
    }

    private DirectExchange exchange() {
        return new DirectExchange(queue);
    }
}
