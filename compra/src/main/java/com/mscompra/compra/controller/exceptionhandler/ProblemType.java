package com.mscompra.compra.controller.exceptionhandler;

import lombok.Getter;


@Getter
public enum ProblemType {

    DADOS_INVALIDOS("/dados-invalidos","Dados invalidos", "Texto"),
    ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema", "Texto"),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parametro invalido", "Texto"),
    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensivel", "Texto"),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado","Recurso nao encontrado", "Texto"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso", "Texto"),
    ERRO_NEGOCIO("/erro-de-negocio", "Violação da regra de negocio", "Texto");

    private final String uri;
    private final String title;

    ProblemType(String uri, String title, String texto){
        this.uri = "/springboot-rest-api-mscompra" + uri;
        this.title = title;
    }

}
