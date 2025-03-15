package com.mscompra.compra.service.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mscompra.compra.model.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


//@RequiredArgsConstructor(onConstructor_ = @Autowired)//Outra maneira de fazer injeção de dependência
//Só para testar vou transformar num endpoint
/*@RequestMapping(value = "/RabbitProducer")
@RestController*/

@Service
public class Producer {

    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    /*Para enviar o objeto para a fila do rabbit teremos que reverter para JSON
      e assim evitar erro de referência do nome do pacote, ex:
      Daqui sai como com.mscompra.compra.model.Pedido e é assim que o Worker-Consumer vai lê-lo e interpretá-lo.
      Mas o pacote no outro microserviço não tem essa nomenclatura, que é com.workercompra.model.Pedido, pois é
      essa entidade(entity PEDIDO) que será procurada no ponto Consumer.
      Quando a comparação for feita e procura desse pacote com a clase for feita, acusará no stacktrace
       Caused by: java.lang.ClassNotFoundException: com.mscompra.compra.model.Pedido
        RESOLVE DA SEGUINTE MANEIRA ABAIXO, se não for passada como string no parâmetro para a fila...
     */

    @SneakyThrows
    public void enviarPedido(@RequestBody Pedido pedido) throws JsonProcessingException {
        //Precisei desserializar o objeto Java para JSON String com ObjectMapper()
        // e inclui a dependência jackson-databind no pom, pois
        // necessito chamar o endpoint do microserviço de notifica email.
        //Mas quando eu implementei o toString() da classe, ele transformava no formato que desconfigurava o JSON, como abaixo:
        //Pedido{codigoPedido=6, nomeCliente='Vienas Matias Per', cpfCliente='044.333.777-47', emailCliente='hdinho321@gmail.com', cep='12.456-450', codigoProduto=8, valor=85, dataCompra=Sat Jan 11 00:01:00 BRT 2025}
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(pedido);//Para converter objeto java em JsonString
        log.info("Ojbeto Java convertido em JSON String: {}", jsonString);
        rabbitTemplate.convertAndSend(queue.getName(), jsonString);//Forma de se passar como string
        log.info(this.getClass().getSimpleName() + ": Enviado JSON String para a fila do Rabbitmq: {}", jsonString);
        //rabbitTemplate.convertAndSend(queue.getName(), objectMapper.writeValueAsString(pedido));
        //Enviado neste formato com toString(): Pedido{codigoPedido=6, nomeCliente='Vienas Matias Per', cpfCliente='044.333.777-47', emailCliente='hdinho321@gmail.com', cep='12.456-450', codigoProduto=8, valor=85, dataCompra=Sat Jan 11 00:01:00 BRT 2025}

    }

}



