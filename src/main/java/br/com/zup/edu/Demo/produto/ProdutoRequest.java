package br.com.zup.edu.Demo.produto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProdutoRequest {
    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

    @NotNull
    private BigDecimal preco;

    @NotBlank
    private String sku;

    public ProdutoRequest(String titulo, String descricao, BigDecimal preco, String sku) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.sku = sku;
    }

    public Produto toModel() {
        return new Produto(titulo, descricao, preco, sku);
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public String getSku() {
        return sku;
    }
}
