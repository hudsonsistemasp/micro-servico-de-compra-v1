package com.mscompra.compra.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;


@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Embeddable
public class Cartao implements Serializable {

    @Serial
    private static final long serialVersionUID = 4163261893845754369L;

    private String numero_cartao;

    private BigDecimal limite_disponivel;

    public Cartao(){
    }

    public Cartao(String numero_cartao, BigDecimal limite_disponivel){
        this.numero_cartao = numero_cartao;
        this.limite_disponivel = limite_disponivel;
    }

    public void setNumero_cartao(String numero_cartao) {
        this.numero_cartao = numero_cartao;
    }
    public String getNumero_cartao() {
        return numero_cartao;
    }

    public void setLimite_disponivel(BigDecimal limite_disponivel) {
        this.limite_disponivel = limite_disponivel;
    }
    public BigDecimal getLimite_disponivel() {
        return limite_disponivel;
    }
}
