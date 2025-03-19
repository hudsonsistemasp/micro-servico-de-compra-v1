package com.mscompra.compra.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data/*Anotação de atalho que agrega as características de @ToString,@EqualsAndHashCode, @Getter / @Setter e @RequiredArgsConstructor*/
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@Table(name = "tb_pedido")
public class Pedido implements Serializable {

    @Serial
    private static final long serialVersionUID = 7142214609110643330L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_pedido")
    private Long codigoPedido;

    //@NotBlank(message = "Precisa preencher o nome do cliente.")
    @NotBlank
    @Size(min = 3, max = 50, message = "O nome precisa ter de 3 a 50 caracteres.")
    private String nomeCliente;

    //@NotBlank(message = "Necessário preenhcer o CPF do cliente.")
    @NotBlank
    //@CPF
    private String cpfCliente;

    @NotBlank
    @Email
    private String emailCliente;

    @NotBlank
    private String cep;

    @Positive
    private Long codigoProduto;

    @NotNull
    @Positive
    private BigDecimal valor;

    @NotNull
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date dataCompra;

    @Embedded
    private Cartao cartao;

    public Long getCodigoPedido() {
        return codigoPedido;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setEmailCliente(String emailCliente){
        this.emailCliente = emailCliente;
    }

    public String getEmailCliente(){
        return emailCliente;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public String getCep() {
        return cep;
    }

    public Long getCodigoProduto() {
        return codigoProduto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setCodigoProduto(Long codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public Cartao getCartao() {
        return cartao;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "codigoPedido=" + codigoPedido +
                ", nomeCliente='" + nomeCliente + '\'' +
                ", cpfCliente='" + cpfCliente + '\'' +
                ", emailCliente='" + emailCliente + '\'' +
                ", cep='" + cep + '\'' +
                ", codigoProduto=" + codigoProduto +
                ", valor=" + valor +
                ", dataCompra=" + dataCompra +
                ", cartao=" + cartao +
                '}';
    }
}
