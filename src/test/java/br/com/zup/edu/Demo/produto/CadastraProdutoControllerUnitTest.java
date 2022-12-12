package br.com.zup.edu.Demo.produto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class CadastraProdutoControllerUnitTest {
    @Mock
    private ProdutoRepository repository;
    private CadastraProdutoController produtoController;

    @Test
    @DisplayName("deve cadastrar um Produto")
    void t1() {
        //Cenario
        this.produtoController = new CadastraProdutoController(repository);
        String sku = "123567";

        ProdutoRequest produtoRequest = new ProdutoRequest(
                "PlayStation 5",
                "Console e 2 controles",
                BigDecimal.TEN,
                sku
        );

        when(repository.existsBySku(sku)).thenReturn(false);
        Produto produto = produtoRequest.toModel();


        when(repository.save(any(Produto.class))).thenReturn(produto);

        //acao e validacao
        ResponseEntity<?> response = produtoController.cadastrar(produtoRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @DisplayName("deve cadastrar um Produto invalido")
    void t2() {
        //Cenario
        this.produtoController = new CadastraProdutoController(repository);

        ProdutoRequest produtoRequest = new ProdutoRequest(
                null,
                null,
                null,
                null
        );

        when(repository.existsBySku(any())).thenReturn(false);
        Produto produto = produtoRequest.toModel();


        when(repository.save(any(Produto.class))).thenReturn(produto);

        //acao e validacao
        ResponseEntity<?> response = produtoController.cadastrar(produtoRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }


    @Test
    @DisplayName("nao deve cadastrar um produto já cadastrado")
    void t3() {
        //Cenario
        this.produtoController = new CadastraProdutoController(repository);
        String sku = "123567";

        ProdutoRequest produtoRequest = new ProdutoRequest(
                "PlayStation 5",
                "Console e 2 controles",
                BigDecimal.TEN,
                sku
        );

        when(repository.existsBySku(sku)).thenReturn(true);

        //acao e validacao
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {

            ResponseEntity<?> response = produtoController.cadastrar(produtoRequest);

        });

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus());
        assertEquals("Produto ja cadastrado", exception.getReason());

    }
}