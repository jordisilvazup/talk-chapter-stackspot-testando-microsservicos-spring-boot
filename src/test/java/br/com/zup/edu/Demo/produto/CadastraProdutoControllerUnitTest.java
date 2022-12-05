package br.com.zup.edu.Demo.produto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CadastraProdutoControllerUnitTest {
    private ProdutoRepository repository;
    private CadastraProdutoController produtoController;

    @Test
    @DisplayName("deve cadastrar um Produto")
    void t1() {
        //Cenario
        this.repository = Mockito.mock(ProdutoRepository.class);
        this.produtoController = new CadastraProdutoController(repository);
        String sku = "123567";

        ProdutoRequest produtoRequest = new ProdutoRequest(
                "PlayStation 5",
                "Console e 2 controles",
                BigDecimal.TEN,
                sku
        );

        Mockito.when(repository.existsBySku(sku)).thenReturn(false);
        Produto produto = produtoRequest.toModel(repository);
        Mockito.verify(repository).existsBySku(sku);

        Mockito.when(repository.save(Mockito.any(Produto.class))).thenReturn(produto);

        //acao e validacao
        ResponseEntity<?> response = produtoController.cadastrar(produtoRequest);


        Mockito.verify(repository).save(Mockito.any(Produto.class));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }


    @Test
    @DisplayName("nao deve cadastrar um produto já cadastrado")
    void t2() {
        //Cenario
        this.repository = Mockito.mock(ProdutoRepository.class);
        this.produtoController = new CadastraProdutoController(repository);
        String sku = "123567";

        ProdutoRequest produtoRequest = new ProdutoRequest(
                "PlayStation 5",
                "Console e 2 controles",
                BigDecimal.TEN,
                sku
        );

        Mockito.when(repository.existsBySku(sku)).thenReturn(true);

        //acao e validacao
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {

            ResponseEntity<?> response = produtoController.cadastrar(produtoRequest);

        });

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus());
        assertEquals("Produto ja cadastrado", exception.getReason());

    }
}