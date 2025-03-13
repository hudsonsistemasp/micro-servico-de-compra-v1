package com.mscompra.compra.service;

/*
1°) A pasta de Teste tem que copiar a estrutura da aplicação oficial
2°) Coloca-se a anotação @ExtendWith(MockitoExtension.class)
3°) A classe que irá ser testada, no caso o PedidoService
4°) Se atentar às injeções de dependência que a classe a ser testada usa. @InjectMocks na classe mãe e o @
5°) Nomenclatura do que se espera do método com a anotação @Test
*/

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mscompra.compra.DadosMock;
import com.mscompra.compra.model.Pedido;
import com.mscompra.compra.repository.PedidoRepository;
import com.mscompra.compra.service.exception.NegocioException;
import com.mscompra.compra.service.rabbitmq.Producer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @InjectMocks //Essa anotação usa-se na classe MÃE. Nas dependências FILHAS, usa-se @Mock
    private static PedidoService pedidoService;

    @Mock //Nas dependências filhas, usa-se @Mock
    private static PedidoRepository pedidoRepository;

    @Mock
    private static Producer producer;

    private static final DadosMock dadosMock = new DadosMock();

//Métodos dos testes agora **************************************************************

    @DisplayName("Salvar Pedido com Sucesso")//6°) Quando rodar o teste, este nome que aparecerá
    @Test
    void deveSalvarPedidoComSucesso() throws JsonProcessingException {

        //var pedidoMock = getPedido();//Uso assim se o método estiver dentro desta classe, como lá embaixo
        //Criar o objeto do contexto principal a ser testado
        var pedidoMock = dadosMock.getPedido();

        //Quando entro no método abaixo, me retorna nullPointer,
        // pois preciso Mockar o retorno dos dados que vem do REPOSITORY.
        //Faz como abaixo, com o Mockito.when()
        //Interpreto: Quando eu chamar o repository, passando a classe Pedido, então tem que me retornar o objeto pedido mockado
        Mockito.when(pedidoRepository.save(Mockito.any(Pedido.class))).thenReturn(pedidoMock);

        //Agora preciso mockar o resultado do producer do Rabbit, pois o Container não está de pé. Todo método que é VOID, basta eu fazer como abaixo:
        Mockito.doNothing().when(producer).enviarPedido(Mockito.any(Pedido.class));

        Pedido pedidoSalvo = pedidoService.salvarPedido(pedidoMock);//7°) Aqui precisamos criar um objeto para passar no parâmetro.
                                                                    //Pode ser como um método abaixo ou uma classe
        //8°) Criar os asserts, que é a comparação do que se espera com o que vai vir e ser comparado
        assertEquals(pedidoMock.getCodigoProduto(), pedidoSalvo.getCodigoProduto());
        assertEquals(pedidoMock.getNomeCliente(), pedidoSalvo.getNomeCliente());
        assertEquals(pedidoMock.getCpfCliente(), pedidoSalvo.getCpfCliente());
        assertNotNull(pedidoSalvo.getCep());

    }

    @DisplayName("Deve Falhar na busca de pedido inexistente por Id")
    @Test
    void deveFalharNaBuscaDePedidoPorId(){
        var id = 1L;

        Throwable negocioException = assertThrows(NegocioException.class, () -> pedidoService.buscarOuFalharPorId(id));

        assertEquals("O pedido com o id: " + id + " nao foi encontrado.", negocioException.getMessage());
    }

    @DisplayName("Deve ter sucesso na busca de pedido por Id")
    @Test
    void deveBuscarPedidoComSucessoPorId(){
        //Vou precisar mockar um pedido
        var pedidoMock = dadosMock.getPedidoSalvo();
        var id = 1L;

        /*Interpreto: Quando eu chamar o repository, passando a classe Pedido, então tem que me retornar o objeto pedido mockado
          Essa é a maneira de mockar o retorno de dados/objeto que vem de uma classe Repository.*/
        Mockito.when(pedidoRepository.findById(id)).thenReturn(Optional.ofNullable(pedidoMock));
        //O findById retorna um Optional, por isso foi necessário implementar Optional.ofNullaable no teste

        var pedidoSalvo = pedidoService.buscarOuFalharPorId(id);

        //assert pedidoMock != null;
        assertNotNull(pedidoMock);
        assertEquals(pedidoMock.getCodigoPedido(), pedidoSalvo.getCodigoPedido());
        assertNotNull(pedidoSalvo);//Verificar se o objeto não veio nulo
        Mockito.verify(pedidoRepository, Mockito.atLeastOnce()).findById(id);//Isso verifica quantas x o método foi chamado com tal parâmetro
    }

    @DisplayName("Deve excluir o pedido com sucesso por id")
    @Test
    void deveExcluirPedidoPorIdComSucesso(){
        //Mockando um objeto pedido
        var pedidoMock = dadosMock.getPedidoSalvo();
        var idPedido = pedidoMock.getCodigoPedido();
        //Mas se eu não mockar um acesso ao Repository, não vai encontrar pedido algum, pois antes de deletar um pedido, este precisa existir
        Mockito.when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedidoMock));
        Mockito.doNothing().when(pedidoRepository).deleteById(idPedido);//Coloco só para desencargo de consciência e nada ser feito no banco
        //Após acessado o Repository, fingindo que um pedido foi encontrado, agora eu o deleto
        pedidoService.deletarPedidoPorId(idPedido);
        //Agora eu verifico se houve, pelo menos, um acesso à camada para deletar o pedido e o sucesso no test feito
        Mockito.verify(pedidoRepository, Mockito.atLeastOnce()).deleteById(idPedido);
    }

    @DisplayName("Deve falhar ao excluir o pedido por id")
    @Test
    void deveFalharAoExcluirPedidoPorId(){
        var id = 1L;
        Mockito.when(pedidoRepository.findById(id)).thenReturn(Optional.empty());
        Throwable negocioException = assertThrows(NegocioException.class, () -> pedidoService.deletarPedidoPorId(id));
        assertEquals("O pedido com o id: " + id + " nao foi encontrado.", negocioException.getMessage());
    }





   /* Pedido getPedido(){
        //Como é uma classe que está embedada, crio aqui para passar como parâmetro
        Cartao cartao = new Cartao("5247 8349 5519 2178", BigDecimal.TEN);

        return Pedido.builder()
                .codigoPedido(1L)
                .nomeCliente("Cliente Classe Teste")
                .cpfCliente("111.222.444-88")
                .emailCliente("paranostodosdigital@gmail.com")
                .cep("88.888-888")
                .codigoProduto(15L)
                .valor(BigDecimal.TEN)
                .dataCompra(new Date())
                .cartao(cartao)
                .build();
    }*/

}
