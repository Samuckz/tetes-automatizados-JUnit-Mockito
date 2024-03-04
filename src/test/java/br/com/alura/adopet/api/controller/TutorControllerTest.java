package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.service.TutorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TutorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TutorService tutorService;

    @Mock
    private TutorRepository tutorRepository;

    @Autowired
    private JacksonTester<CadastroTutorDto> jsonDtoCadastrar;

    @Autowired
    private JacksonTester<AtualizacaoTutorDto> jsonDtoAtualizar;

    // === CADASTRAR ===

    @Test
    @DisplayName("[CADASTRAR] - Must return code 200 when register a new Tutor")
    void A00_mustReturnCode200WhenRegisterNewTutor() throws Exception{
        // ARRANGE
        CadastroTutorDto dto = new CadastroTutorDto(
                "Robert",
                "3198541458",
                "robert@test.com"
        );

        //ACT
        var response = mvc.perform(
                post("/tutores")
                        .content(jsonDtoCadastrar.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("[CADASTRAR] - Must return code 400 when register a new Tutor with errors")
    void A01_mustReturnCode400WhenRegisterNewTutorWithErrors() throws Exception{
        // ARRANGE
        var json = "{}";

        //ACT
        var response = mvc.perform(
                post("/tutores")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    // === ATUALIZAR ===
    @Test
    @DisplayName("[ATUALIZAR] - Must return code 200 when update a Tutor")
    void A02_mustReturnCode200WhenUpdateTutor() throws Exception{
        // ARRANGE
        AtualizacaoTutorDto dto = new AtualizacaoTutorDto(
                2L,
                "Luizão",
                "3198775621",
                "luizaodagalera@gmail.com"
                );

        //ACT
        var response = mvc.perform(
                put("/tutores")
                        .content(jsonDtoAtualizar.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("[ATUALIZAR] - Must return code 400 when update a Tutor with errors")
    void A03_mustReturnCode400WhenUpdateTutorWithErrors() throws Exception{
        // ARRANGE
        AtualizacaoTutorDto dto = new AtualizacaoTutorDto(
                2L,
                "Luizão",
                "319875475621",
                "luizaodagalera@gmail.com"
        );

        //ACT
        var response = mvc.perform(
                post("/tutores")
                        .content(jsonDtoAtualizar.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }


}