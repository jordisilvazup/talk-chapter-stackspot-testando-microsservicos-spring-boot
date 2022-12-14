package br.com.zup.edu.Demo.produto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
class CadastraProdutoControllerUnitTest {
    @Mock
    private ProdutoRepository repository;
    private CadastraProdutoController produtoController;

    @BeforeEach
    void setUp() {
        this.produtoController = new CadastraProdutoController(repository);
    }

    @Test
    @DisplayName("deve cadastrar um Produto")
    void t1() {
        //Cenario
        ProdutoRequest produtoRequest = new ProdutoRequest(
                "PlayStation 5",
                "Console e 2 controles",
                BigDecimal.TEN,
                "123567"
        );
        when(repository.existsBySku(produtoRequest.getSku())).thenReturn(false);
        when(repository.save(any(Produto.class))).thenReturn(produtoRequest.toModel());

        //acao
        ResponseEntity<?> response = produtoController.cadastrar(produtoRequest);

        // validacao
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    @DisplayName("deve cadastrar um Produto invalido")
    void t2() {
        //Cenario
        ProdutoRequest produtoRequest = new ProdutoRequest(
                null,
                null,
                null,
                null
        );
        when(repository.existsBySku(any())).thenReturn(false);
        when(repository.save(any(Produto.class))).thenReturn(produtoRequest.toModel());
        //acao
        ResponseEntity<?> response = produtoController.cadastrar(produtoRequest);
        //validacao
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }


    @Test
    @DisplayName("nao deve cadastrar um produto já cadastrado")
    void t3() {
        //Cenario
        ProdutoRequest produtoRequest = new ProdutoRequest(
                "PlayStation 5",
                "Console e 2 controles",
                BigDecimal.TEN,
                "1234567"
        );

        when(repository.existsBySku(produtoRequest.getSku())).thenReturn(true);

        //acao
        ResponseStatusException responsException = assertThrows(ResponseStatusException.class, () -> {
            produtoController.cadastrar(produtoRequest);
        });

        //validacao
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responsException.getStatus());
        assertEquals("Produto ja cadastrado", responsException.getReason());

    }
}