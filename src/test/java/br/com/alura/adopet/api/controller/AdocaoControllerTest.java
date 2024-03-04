package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.service.AdocaoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AdocaoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean // Spring configura e injeta no controller automaticamente
    private AdocaoService service;

    @Autowired
    private JacksonTester<SolicitacaoAdocaoDto> jsonDtoSolicitacao;

    @Autowired
    private JacksonTester<AprovacaoAdocaoDto> jsonDtoAprovacao;

    @Autowired
    private JacksonTester<ReprovacaoAdocaoDto> jsonDtoReprovacao;

    // === POST METHODS ===


    @Test
    @Order(1)
    @DisplayName("[SOLICITAR] - Must return code 400 when request an adoption")
    void deveriaRetornarCodigo400PAraSolicitacaoDeAdocaoComErros() throws Exception {

        // ARRANGE
        String json = "{}";

        //ACT
        var response = mvc.perform(
            post("/adocoes")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @Order(2)
    @DisplayName("[SOLICITAR] - Must return code 200 when request an adoption")
    void deveriaRetornarCodigo200PAraSolicitacaoDeAdocaoSemErros() throws Exception {

        // ARRANGE
        String json = """
                {
                    "idPet": 2,
                    "idTutor": 3,
                    "motivo": "Motivo qualquer"
                }
                """;

        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(2L, 3L, "Motivo qualquer");

        //ACT
        var response = mvc.perform(
                post("/adocoes")
                        .content(jsonDtoSolicitacao.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    // === APROVAÇÃO ===

    @Test
    @Order(3)
    @DisplayName("[APROVAR] - Must return code 200 when aprove an adoption with erros")
    void mustReturn200WhenAdoptionAprovationsWasSent() throws Exception {
        // ARRANGE
        AprovacaoAdocaoDto dto = new AprovacaoAdocaoDto(2L);

        // ACT
        var response = mvc.perform(
                put("/adocoes/aprovar")
                        .content(jsonDtoAprovacao.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());


    }

    @Test
    @Order(4)
    @DisplayName("[APROVAR] - Must return code 400 when aprove an adoption with erros")
    void mustReturn400WhenAdoptionAprovationsWasSentWithErrors() throws Exception {
        // ARRANGE
        String json = "{}";

        // ACT
        var response = mvc.perform(
                put("/adocoes/aprovar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(400, response.getStatus());


    }

    // === REPROVAR ===

    @Test
    @Order(5)
    @DisplayName("[REPROVAR] - Must return code 200 when reprove an adoption succefuly")
    void mustReturn200WhenReprovationWasSent() throws Exception{
        // ARRANGE
        ReprovacaoAdocaoDto dto = new ReprovacaoAdocaoDto(2L, "Justificativa qualquer");

        //ACT
        var response = mvc.perform(
             put("/adocoes/reprovar")
                     .content(jsonDtoReprovacao.write(dto).getJson())
                     .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERTIONS
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @Order(6)
    @DisplayName("[REPROVAR] - Must return code 400 when reprove an adoption with errors")
    void mustReturn400WhenReprovationWasSentWithErrors() throws Exception{
        // ARRANGE
        String json = "{}";

        //ACT
        var response = mvc.perform(
                put("/adocoes/reprovar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERTIONS
        Assertions.assertEquals(400, response.getStatus());
    }

}