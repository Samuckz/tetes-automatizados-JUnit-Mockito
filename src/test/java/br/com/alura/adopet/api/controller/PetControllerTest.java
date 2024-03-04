package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.CadastroPetDto;
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
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class PetControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PetService petService;

    @Autowired
    private JacksonTester<CadastroPetDto> jsonDto;

    @Test
    @DisplayName("[LISTAR] - Must return code 200 when list all pets available")
    void A0_mustReturnCode200WhenListAllPetsAvailable() throws Exception {
        var response = mvc.perform(
                get("/pets")
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
    }

}