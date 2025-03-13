package com.mscompra.compra.service.exception;

public class EntidadeNaoEncontradaException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public EntidadeNaoEncontradaException(String message){
        super(message);
    }

    public EntidadeNaoEncontradaException(String message, Throwable causa){
        super(message, causa);
    }
}
