package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.ProbabilidadeAdocao;
import br.com.alura.adopet.api.model.TipoPet;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalculadoraProbabilidadeAdocaoTest {
    @Test
    @DisplayName("Probabilidade de adoção alta para gatos com idade baixa e peso baixo")
    void Test01_ProbabilidadeDeAdocaoAlta(){
        // idade 4 anos e peso de 4Kg - Probabilidae ALTA
        CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();

        Abrigo abrigo = new Abrigo(new CadastroAbrigoDto(
                "Abrigo feliz",
                "94999999999",
                "abrigofeliz@email.com.br"
        ));
        Pet pet = new Pet(new CadastroPetDto(
                TipoPet.GATO,
                "Miau",
                "Siames",
                4,
                "Cinza",
                4.0f
        ), abrigo);

        var probabilidade = calculadora.calcular(pet);

        Assertions.assertEquals(ProbabilidadeAdocao.ALTA, probabilidade);

    }

    @Test
    @DisplayName("Probabilidade de adoção media para gatos com idade alta e peso baixo")
    void Test02_ProbabilidadeDeAdocaoMedia(){
        // idade 12 anos e peso de 4Kg - Probabilidade MEDIA
        CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();

        Abrigo abrigo = new Abrigo(new CadastroAbrigoDto(
                "Abrigo feliz",
                "94999999999",
                "abrigofeliz@email.com.br"
        ));
        Pet pet = new Pet(new CadastroPetDto(
                TipoPet.GATO,
                "Miau",
                "Siames",
                15,
                "Cinza",
                4.0f
        ), abrigo);

        var probabilidade = calculadora.calcular(pet);

        Assertions.assertEquals(ProbabilidadeAdocao.MEDIA, probabilidade);

    }

    @Test
    @DisplayName("Probabilidade de adoção baixa para gatos com idade alta e peso alto")
    void Test03_ProbabilidadeDeAdocaoBaixa(){
        // idade 12 anos e peso de 12Kg - Probabilidae Medica
        CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();

        Abrigo abrigo = new Abrigo(new CadastroAbrigoDto(
                "Abrigo feliz",
                "94999999999",
                "abrigofeliz@email.com.br"
        ));
        Pet pet = new Pet(new CadastroPetDto(
                TipoPet.GATO,
                "Miau",
                "Siames",
                12,
                "Cinza",
                12.0f
        ), abrigo);

        var probabilidade = calculadora.calcular(pet);

        Assertions.assertEquals(ProbabilidadeAdocao.BAIXA, probabilidade);

    }
}