package com.mscompra.compra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mscompra.compra.model.Pedido;
import com.mscompra.compra.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(PedidoController.class.getName());

    @Autowired
    private PedidoService pedidoService;

    @PostMapping(value = "/salvarPedido", produces = "application/json")
    public ResponseEntity<Pedido> salvar(@RequestBody @Valid Pedido pedido) throws JsonProcessingException {

       /* try {
            Pedido pedidoSalvo = pedidoService.salvarPedido(pedido);
            return ResponseEntity.ok(pedidoSalvo);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }*/

        Pedido pedidoSalvo = pedidoService.salvarPedido(pedido);
        log.info(this.getClass().getSimpleName() + " : Pedido Salvo no banco de dados.");
        return ResponseEntity.ok(pedidoSalvo);
    }


    @GetMapping("/buscarPedidoPorId/{idPedido}")
    public ResponseEntity<Optional<Pedido>> buscarPedidoPorId(@PathVariable Long idPedido){
        Optional<Pedido> pedido = pedidoService.buscarPedidoPorId(idPedido);
        //return ResponseEntity.ok(pedido);//ou
        return ResponseEntity.ok().body(pedido);
    }

    //Assinatura void com ResponseStatus pela anotação abaixo
    @DeleteMapping("/deletarPedido/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPedidoPorId(@PathVariable Long id){
        pedidoService.deletarPedidoPorId(id);
    }

    /*
    //Assinatura com Response para o client
    @DeleteMapping("/deletarPedido/{id}")
    public ResponseEntity<String> deletarPedidoPorId(@PathVariable Long id){
        pedidoService.deletarPedidoPorId(id);
        return ResponseEntity.ok().body("Pedido " + id + " delatado com sucesso");//
    }
    */
}
