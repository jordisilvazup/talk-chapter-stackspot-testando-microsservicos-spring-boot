package br.com.zup.edu.Demo.produto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.zalando.problem.spring.common.MediaTypes;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class CadastraProdutoControllerIntegrationTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("deve cadastrar um Produto")
    void t1() throws Exception {
        //Cenario
        ProdutoRequest produtoRequest = new ProdutoRequest(
                "PlayStation 5",
                "Console e 2 controles",
                BigDecimal.TEN,
                "123456"
        );

        String payload = toJson(produtoRequest);

        MockHttpServletRequestBuilder request = post("/produtos")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .contentType(APPLICATION_JSON)
                .content(payload);

        //Acao
        ResultActions response = mockMvc.perform(request);

        //Validacao
        response.andExpectAll(
                status().isCreated()
        );

        assertEquals(
                1,
                repository.findAll().size(),
                "deveria conter apenas um registro de produto"
        );

    }

    @Test
    @DisplayName("nao deve cadastrar um Produto invalido")
    void t2() throws Exception {
        //Cenario
        ProdutoRequest produtoRequest = new ProdutoRequest(
                null,
                null,
                null,
                null
        );

        String payload = toJson(produtoRequest);

        MockHttpServletRequestBuilder request = post("/produtos")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .contentType(APPLICATION_JSON)
                .content(payload);

        //Acao
        ResultActions response = mockMvc.perform(request);

        //Validacao
        response.andExpectAll(
                status().isBadRequest(),
                header().string(HttpHeaders.CONTENT_TYPE, is(MediaTypes.PROBLEM_VALUE)),
                jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")),
                jsonPath("$.title", is("Constraint Violation")),
                jsonPath("$.status", is(400)),
                jsonPath("$.violations", hasSize(4)),
                jsonPath("$.violations", containsInAnyOrder(
                                violation("descricao", "must not be blank"),
                                violation("sku", "must not be blank"),
                                violation("titulo", "must not be blank"),
                                violation("preco", "must not be null")
                        )
                )
        );

    }

    @Test
    @DisplayName("nao deve cadastrar um produto já cadastrado")
    void t3() throws Exception {
        //Cenario
        String sku = "123456";
        Produto produto = new Produto("PlayStation 5", "Console", BigDecimal.ONE, sku);
        repository.save(produto);

        ProdutoRequest produtoRequest = new ProdutoRequest(
                "PlayStation 5",
                "Console e 2 controles",
                BigDecimal.TEN,
                sku
        );

        String payload = toJson(produtoRequest);

        MockHttpServletRequestBuilder request = post("/produtos")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .contentType(APPLICATION_JSON)
                .content(payload);

        //Acao
        ResultActions response = mockMvc.perform(request);

        //Validacao
        response.andExpectAll(
                status().isUnprocessableEntity(),
                header().string(HttpHeaders.CONTENT_TYPE, is(MediaTypes.PROBLEM_VALUE)),
                jsonPath("$.title", is("Unprocessable Entity")),
                jsonPath("$.status", is(422)),
                jsonPath("$.detail", is("Produto ja cadastrado"))
        );
    }

    private <T> String toJson(T objetct) throws JsonProcessingException {
        return mapper.writeValueAsString(objetct);
    }

    private Map<String, Object> violation(String field, String message) {
        return Map.of(
                "field", field,
                "message", message
        );
    }

}