package br.com.zup.edu.Demo.pagamento;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NovoPagamentoControllerUnitTest {
    private PagamentoProducer producer;
    private NovoPagamentoController pagamentoController;


    @Test
    @DisplayName("deve criar um novo Pagamento")
    void t1() {
        //Cenario
        this.producer = Mockito.mock(PagamentoProducer.class);
        this.pagamentoController = new NovoPagamentoController(producer);

        UUID vendaId = UUID.randomUUID();
        PagamentoRequest pagamentoRequest = new PagamentoRequest(vendaId, "Spotify", BigDecimal.TEN);


        //Acao
        Mockito.doNothing().when(producer).send(Mockito.any(PagamentoEvent.class));
        ResponseEntity<?> response = pagamentoController.pagar(pagamentoRequest);

        //Validacao
        Mockito.verify(producer).send(Mockito.any(PagamentoEvent.class));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @Test
    @DisplayName("nao deve criar um Pagamento invalido")
    void t2() {
        //Cenario
        this.producer = Mockito.mock(PagamentoProducer.class);
        this.pagamentoController = new NovoPagamentoController(producer);

        UUID vendaId = UUID.randomUUID();
        PagamentoRequest pagamentoRequest = new PagamentoRequest(null, null, null);


        //Acao
        Mockito.doNothing().when(producer).send(Mockito.any(PagamentoEvent.class));
        ResponseEntity<?> response = pagamentoController.pagar(pagamentoRequest);

        //Validacao
        Mockito.verify(producer).send(Mockito.any(PagamentoEvent.class));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}