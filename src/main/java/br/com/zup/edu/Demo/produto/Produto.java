package br.com.zup.edu.Demo.produto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Produto {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(nullable = false, unique = true)
    private String sku;


    public Produto(String titulo, String descricao, BigDecimal preco, String sku) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.sku = sku;
    }

    @Deprecated
    public Produto() {
    }


    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", sku='" + sku + '\'' +
                '}';
    }

}
