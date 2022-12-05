package br.com.zup.edu.Demo.pagamento;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public class PagamentoEvent {
    @NotNull
    private UUID id;
    @NotNull
    private UUID vendaId;
    @NotBlank
    private String descricao;
    @NotNull
    private BigDecimal valor;

    public PagamentoEvent(UUID vendaId, String descricao, BigDecimal valor) {
        this.id=UUID.randomUUID();
        this.vendaId = vendaId;
        this.descricao = descricao;
        this.valor = valor;
    }

    /**
     * @deprecated construtor para uso exclusivo do Jackson
     */
    @Deprecated
    public PagamentoEvent() {
    }

    public UUID getId() {
        return id;
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

    @Override
    public String toString() {
        return "PagamentoEvent{" +
                "id=" + id +
                ", vendaId=" + vendaId +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                '}';
    }
}
