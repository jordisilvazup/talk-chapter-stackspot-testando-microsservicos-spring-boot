package br.com.zup.edu.Demo.pagamento;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class NovoPagamentoControllerUnitTest {
    @Mock
    private PagamentoProducer producer;
    private NovoPagamentoController pagamentoController;


    @Test
    @DisplayName("deve criar um novo Pagamento")
    void t1() {
        //Cenario
        this.pagamentoController = new NovoPagamentoController(producer);

        UUID vendaId = UUID.randomUUID();
        PagamentoRequest pagamentoRequest = new PagamentoRequest(vendaId, "Spotify", BigDecimal.TEN);

        //Acao
        doNothing().when(producer).send(any(PagamentoEvent.class));
        ResponseEntity<?> response = pagamentoController.pagar(pagamentoRequest);

        //Validacao
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @Test
    @DisplayName("nao deve criar um Pagamento invalido")
    void t2() {
        //Cenario
        this.pagamentoController = new NovoPagamentoController(producer);

        UUID vendaId = UUID.randomUUID();
        PagamentoRequest pagamentoRequest = new PagamentoRequest(null, null, null);


        //Acao
        doNothing().when(producer).send(any(PagamentoEvent.class));
        ResponseEntity<?> response = pagamentoController.pagar(pagamentoRequest);

        //Validacao
        verify(producer).send(any(PagamentoEvent.class));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}