package com.mscompra.compra.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mscompra.compra.repository.PedidoRepository;
import com.mscompra.compra.service.exception.EntidadeNaoEncontradaException;
import com.mscompra.compra.service.exception.NegocioException;
import com.mscompra.compra.service.rabbitmq.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mscompra.compra.model.Pedido;

import java.util.Optional;


@Service
public class PedidoService {
    //@Log e @Slf4j, do lombok, não estão funcionando nem a pau.
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(PedidoService.class.getName());

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private Producer producer;

    public Pedido salvarPedido(Pedido pedido) throws JsonProcessingException {
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        log.info(this.getClass().getName() + " :Pedido foi salvo no banco de dados.");
        producer.enviarPedido(pedidoSalvo); //Chamada ao Rabbitmq
        log.info(this.getClass().getName() + " : Producer enviou o pedido para a fila do rabbitmq");
        return pedidoSalvo;
    }

    public Pedido buscarOuFalharPorId(Long id){
        return pedidoRepository.findById(id)
               .orElseThrow( () -> new NegocioException("O pedido com o id: " + id + " nao foi encontrado.") );

}

public Optional<Pedido> buscarPedidoPorId(Long idDoPedido){
    return Optional.ofNullable(pedidoRepository.findById(idDoPedido)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("O pedido com o id: " + idDoPedido + " nao foi encontrado.")));
}

public void deletarPedidoPorId(Long id){
        //Primeiro verificar se o pedido existe, por que não faz sentido deletar o que não for encontrado
        Pedido pedido = buscarOuFalharPorId(id);
        pedidoRepository.deleteById(id);
    }

}
