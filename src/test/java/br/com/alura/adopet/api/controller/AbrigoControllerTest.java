package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AbrigoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AbrigoService abrigoService;

    @MockBean
    private PetService petService;

    @Autowired
    private JacksonTester<CadastroAbrigoDto> jsonDtoCadastrar;

    @Autowired
    private JacksonTester<CadastroPetDto> jsonDtoPet;

    // === CADASTRAR ABRIGO ===

    @Test
    @DisplayName("[CADASTRAR ABRIGO] - Must return code 200 when register a new shelter successfully")
    void mustReturnCode200WhenRegisterShelterSuccessfully() throws Exception{
        //ARRANGE
        CadastroAbrigoDto dto = new CadastroAbrigoDto(
                "Abrigo Feliz",
                "1234567890",
                "abrigofeliz@teste.com"
        );

        //ACT
        var response = mvc.perform(
                post("/abrigos")
                        .content(jsonDtoCadastrar.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("[CADASTRAR ABRIGO] - Must return code 400 when register a new shelter with erros")
    void mustReturnCode400WhenRegisterShelterWithErrors() throws Exception{
        //ARRANGE
        String json = "{}";

        //ACT
        var response = mvc.perform(
                post("/abrigos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    // === LISTAR PETS ===

    @Test
    @DisplayName("[LISTAR PETS] - Must return code 200 when list all pets of an shelter")
    void mustReturncode200WhenListPets() throws Exception {
        //ARRANGE
        String idOuNome = "1";

        //ACT
        var response = mvc.perform(
                get("/abrigos/" + idOuNome + "/pets")
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("[LISTAR PETS] - Must return code 404 when list all pets of an shelter when shelter nameOrId was not found")
    void mustReturnCode404WhenListPetsWithError() throws Exception {
        //ARRANGE
        String idOuNome = "1";
        given(abrigoService.listarPetsDoAbrigo(idOuNome)).willThrow(ValidacaoException.class);

        //ACT
        var response = mvc.perform(
                get("/abrigos/" + idOuNome + "/pets")
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    // === CADASTRAR PET ===

    @Test
    @DisplayName("[CADASTRAR PET] - Must return code 200 when register a new pet for a register")
    void mustReturn200WhenRegisterANewPetForAShelter() throws Exception {
        // ARRANGE
        CadastroPetDto dto = new CadastroPetDto(
                TipoPet.CACHORRO,
                "Rex",
                "Vira-lata",
                2,
                "Marrom",
                8.5f
        );

        String idOuNome = "1";

        //ACT
        var response = mvc.perform(
                post("/abrigos/" + idOuNome + "/pets")
                        .content(jsonDtoPet.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("[CADASTRAR PET] - Must return code 404 when register a new pet for a register with errors")
    void mustReturn400WhenRegisterANewPetForAShelterWithErrors() throws Exception {
        // ARRANGE
        CadastroPetDto dto = new CadastroPetDto(
                TipoPet.CACHORRO,
                "Rex",
                "Vira-lata",
                2,
                "Marrom",
                8.5f
        );

        String idOuNome = "1";

        //ACT

        given(abrigoService.carregarAbrigo(idOuNome)).willThrow(ValidacaoException.class);

        var response = mvc.perform(
                post("/abrigos/" + idOuNome + "/pets")
                        .content(jsonDtoPet.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    // === LISTAR ===

    @Test
    @DisplayName("[LISTAR ABRIGOS] - Must return code 200 when list all shelters")
    void mustReturnCode200WhenListAllShelters() throws Exception {
        // ARRANGE

        //ACT
        var response = mvc.perform(
                get("/abrigos")
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }


}