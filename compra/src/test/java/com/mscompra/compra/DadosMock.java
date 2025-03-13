package com.mscompra.compra;

import com.mscompra.compra.model.Cartao;
import com.mscompra.compra.model.Pedido;

import java.math.BigDecimal;
import java.util.Date;

public class DadosMock {

    //Methods
    //Como é uma classe que está embedada, crio aqui para passar como parâmetro
    Cartao cartao = new Cartao("5247 8349 5519 2178", BigDecimal.TEN);

    //Este simula um pedido a ser enviado ao banco de dados
    public Pedido getPedido(){
        return Pedido.builder()
                .nomeCliente("Pedido Classe Teste")
                .cpfCliente("111.222.444-88")
                .emailCliente("paranostodosdigital@gmail.com")
                .cep("88.888-888")
                .codigoProduto(15L)
                .valor(BigDecimal.TEN)
                .dataCompra(new Date())
                .cartao(cartao)
                .build();
    }

    //Este simula um pedido que já foi salvo no banco de dados, pois já possui o codidoPedido
    public Pedido getPedidoSalvo(){
        return Pedido.builder()
                .codigoPedido(1L)
                .nomeCliente("Pedido Classe Teste")
                .cpfCliente("111.222.444-88")
                .emailCliente("paranostodosdigital@gmail.com")
                .cep("88.888-888")
                .codigoProduto(15L)
                .valor(BigDecimal.TEN)
                .dataCompra(new Date())
                .cartao(cartao)
                .build();
    }

}
