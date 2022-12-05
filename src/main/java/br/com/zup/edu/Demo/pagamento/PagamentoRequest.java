package br.com.zup.edu.Demo.pagamento;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public class PagamentoRequest {
    @NotNull
    private UUID vendaId;
    @NotBlank
    private String descricao;
    @NotNull
    private BigDecimal valor;

    public PagamentoRequest(UUID vendaId, String descricao, BigDecimal valor) {
        this.vendaId = vendaId;
        this.descricao = descricao;
        this.valor = valor;
    }

    public PagamentoEvent toEvent() {
        return new PagamentoEvent(vendaId, descricao, valor);
    }

    public UUID getVendaId() {
        return vendaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
