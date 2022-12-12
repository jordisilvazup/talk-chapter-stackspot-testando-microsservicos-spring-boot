package br.com.zup.edu.Demo.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
/*
    Since most repository calls are for read operations, it is good practice
    to define at the class level that transactions are read-only.
    reference: https://vladmihalcea.com/spring-transaction-best-practices/
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsBySku(String sku);
}
