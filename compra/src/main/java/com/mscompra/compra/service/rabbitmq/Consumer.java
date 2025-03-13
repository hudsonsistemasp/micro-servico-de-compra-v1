package com.mscompra.compra.service.rabbitmq;

/*Tirei essa programação local, criei um micro serviço Worker à parte para consumir essa fila*/


import com.mscompra.compra.model.Pedido;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
/*

@Component//Vamos deixar esse Consumer com o comportamento de listener para consumir o payload
public class Consumer {

    //Por ser um array, pode-se ouvir várias filas:..(queues = "${queue.name1}", "${queue.name2}",...})
    @RabbitListener(queues = {"${queue.name}"})
    public void consumer(@Payload String corpoDaRequisicao){
        System.out.println("Mensagem recebida:" + corpoDaRequisicao);
    }

}

*/