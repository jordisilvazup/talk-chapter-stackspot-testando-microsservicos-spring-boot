package br.com.zup.edu.Demo.produto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
public class CadastraProdutoController {
    private final ProdutoRepository repository;
    private final Logger LOGGER = LoggerFactory.getLogger(CadastraProdutoController.class);

    public CadastraProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/produtos")
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid ProdutoRequest request) {
        Produto novoProduto = request.toModel(repository);
        repository.save(novoProduto);

        LOGGER.info("Produto Cadastrado {}",novoProduto);


        return ResponseEntity.status(CREATED).build();
    }
}
