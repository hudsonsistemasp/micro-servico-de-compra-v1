package com.mscompra.compra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mscompra.compra.CompraApplication;
import com.mscompra.compra.DadosMock;
import com.mscompra.compra.model.Pedido;
import com.mscompra.compra.repository.PedidoRepository;
import com.mscompra.compra.service.PedidoService;

import com.mscompra.compra.service.exception.EntidadeNaoEncontradaException;
import com.mscompra.compra.service.exception.NegocioException;
import com.mscompra.compra.service.rabbitmq.Producer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//CLASSE PARA TESTE DE INTEGRAÇÃO

@ExtendWith(SpringExtension.class)//
@SpringBootTest(classes = CompraApplication.class)//Aqui coloca a classe principal da aplicação, pois será necessário subir um contexto.
@AutoConfigureMockMvc//Framework para Mockar os dados para fazer os requests
@ActiveProfiles("test")//É aqui que eu chamo o perfil que quero rodar o teste, tipo o que está citado no application.properties
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;//É o objeto que simula as requisições aos nosso endpoints

    private static final DadosMock dadosMock = new DadosMock();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    //Agora pego os objetos que o Service usa: Repository, Producer do Rabbitmq, Objeto pedido
    @InjectMocks
    private PedidoService pedidoService; //O @Autowired eu uso na classe MÃE, nas filhas usa @MockitoBean

    @Mock
    private PedidoRepository pedidoRepository;

    /*Temos um desafio quando acionamos a camada Service e esta chama o PRODUCER do RABBITMQ, pois se o container
     * não estiver em pé, dará erro ao fazer a requisição. Ou sobe a imagem ou mocka o dado,
     * seja realizando o teste no localhost ou quando for realizar o build no gitActions
     */

    //Mockando o dado do rabbitmq e assim evitar erro, com isso não precisa subir a imagem do docker, mas é uma opção
    @Mock
    private Producer producer; //Nas dependências FILHAS, usa-se @MockitoBean


    private static final String ROTA_SALVAR_PEDIDO = "/pedido/salvarPedido";
    private static final  String ROTA_BUSCAR_PEDIDO_POR_ID = "/pedido/buscarPedidoPorId";
    private static final String ROTA_EXCLUIR_PEDIDO_POR_ID = "/pedido//deletarPedido/";

    //Métodos dos testes agora **************************************************************

    @DisplayName("POST - Salvar Pedido com sucesso")
    @Test
    void salvarPedidoComSucesso() throws JsonProcessingException {
        var pedidoMock = dadosMock.getPedido();

        Mockito.when(pedidoRepository.save(Mockito.any(Pedido.class))).thenReturn(pedidoMock);
        Mockito.doNothing().when(producer).enviarPedido(Mockito.any(Pedido.class));//Mockar o retorno da chamada ao PRODUCER do RABBITMQ

        var pedidoSalvo = pedidoService.salvarPedido(pedidoMock);

        assertEquals(pedidoMock.getCodigoProduto(), pedidoSalvo.getCodigoProduto());
        assertNotNull(pedidoSalvo.getCodigoProduto());

    }
    //******************************************************************************************
    @DisplayName("GET - Deve buscar o pedido por ID com sucesso na base de dados")
    @Test
    void deveBuscarPedidoPorIdComSucesso() throws Exception {
        var idDoPedido = 200L;
        mockMvc.perform(get(ROTA_BUSCAR_PEDIDO_POR_ID.concat("/" + idDoPedido)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //******************************************************************************************

    @DisplayName("GET - Deve falhar ao buscar o pedido por ID que não existe ")
    @Test
    void deveFalharAoBuscarPedidoQueNaoExiste() throws Exception {
        var idDoPedido = 10L;

        //Constatar que a operação deu certo, ou seja, falhou ao tentar achar um pedido com tal código
        Throwable exception = assertThrows(EntidadeNaoEncontradaException.class, () -> {
            pedidoService.buscarPedidoPorId(idDoPedido);
        });
        //Ver se as mensagens do que esperamos é a mesma do que foi retornado
        assertEquals("O pedido com o id: " + idDoPedido + " nao foi encontrado.", exception.getMessage());

    }
    //******************************************************************************************
    @DisplayName("GET - Deve falhar ao excluir um pedido que não existe")
    @Test
    void deveFalharAoExcluirPedidoQueNaoExiste(){
        var idDoPedido = 15L;

        Mockito.when(pedidoRepository.findById(idDoPedido)).thenReturn(Optional.empty());

        //Constatar que a operação deu certo, ou seja, falhou ao tentar excluir um pedido com idDoPedido
        Throwable exception = assertThrows(EntidadeNaoEncontradaException.class, () -> {
            pedidoService.buscarPedidoPorId(idDoPedido);
        });

        //Ver se as mensagens do que esperamos é a mesma do que foi retornado
        assertEquals("O pedido com o id: " + idDoPedido + " nao foi encontrado.", exception.getMessage());
    }

    //******************************************************************************************
    @DisplayName("DELETE - Deve excluir um pedido com sucesso por ID")
    @Test
    void deveExcluirUmPedidoComSucesso() throws Exception {
        //um id para mockar o objeto a ser excluído
        var idDoPedido = 200L;
        //Simular uma requisição ao endpoint
        mockMvc.perform(delete(ROTA_EXCLUIR_PEDIDO_POR_ID.concat("/" + idDoPedido)))
                .andDo(print())
                .andExpect(status().isNoContent());
    //Agora um assert para constatar que a operação deu certo, basta eu procurar o ID do pedido e este não ser encontrado
        Throwable exception = assertThrows(EntidadeNaoEncontradaException.class, () -> pedidoService.buscarPedidoPorId(idDoPedido));
        //Agora verificar se a mensagem é a mesma do esperado e do que foi retornado
        assertEquals("O pedido com o id: " + idDoPedido + " nao foi encontrado.", exception.getMessage());


    }




}
