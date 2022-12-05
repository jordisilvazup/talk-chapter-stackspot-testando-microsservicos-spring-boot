package br.com.zup.edu.Demo.pagamento;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
public class NovoPagamentoController {
    private final PagamentoProducer pagamentoProducer;

    public NovoPagamentoController(PagamentoProducer pagamentoProducer) {
        this.pagamentoProducer = pagamentoProducer;
    }

    @PostMapping("/pagamentos")
    public ResponseEntity<?> pagar(@RequestBody @Valid PagamentoRequest request){
        PagamentoEvent pagamentoEvent = request.toEvent();

        pagamentoProducer.send(pagamentoEvent);


        return ResponseEntity.status(CREATED).build();
    }
}
