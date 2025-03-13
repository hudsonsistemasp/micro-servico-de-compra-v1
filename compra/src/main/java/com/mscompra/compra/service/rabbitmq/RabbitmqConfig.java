package com.mscompra.compra.service.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Cria-se este config quando tem um PRODUCER
@Configuration
public class RabbitmqConfig {

    @Value("${queue.name}")//pega o parâmetro que está no application(-test).properties
    private String queueName;

    @Bean
    public Queue queue(){
        return new Queue(queueName, true);
    }


}
