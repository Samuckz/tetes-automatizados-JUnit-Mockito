package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AdocaoServiceTest {

    @InjectMocks
    private AdocaoService service;

    @Mock
    private AdocaoRepository repository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private EmailService emailService;

    @Spy // Instancia realmente um objeto, mas ele ainda será controlado pelos testes
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();

    @Mock
    private ValidacaoSolicitacaoAdocao validador1;
    @Mock
    private ValidacaoSolicitacaoAdocao validador2;

    @Mock
    private Pet pet;

    @Mock
    private Tutor tutor;

    @Mock
    private Abrigo abrigo;

    private SolicitacaoAdocaoDto dto;

    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;

    // === SOLICITAR ADOÇÃO ===

    @Test
    @DisplayName("Após solicitação, a adoção deveria ser salva no banco de dados")
    void deveriaSalvarAdocaoAoSolicitar(){

        //ARRANGE
        this.dto = new SolicitacaoAdocaoDto(10L, 20L, "motivo qualquer");

        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);


        //ACT
        service.solicitar(dto);
        //ASSERT -> Neste caso, usaremos o trhen pois queremos fazer uma verificação sobre um mock, não sobre um instancia
        then(repository).should().save(adocaoCaptor.capture());
        Adocao adocaoSalve = adocaoCaptor.getValue();


        Assertions.assertEquals(pet, adocaoSalve.getPet());
        Assertions.assertEquals(tutor, adocaoSalve.getTutor());
        Assertions.assertEquals(dto.motivo(), adocaoSalve.getMotivo());

    }

    @Test
    @DisplayName("Após solicitação, os validadores devem ser chamados")
    void deveriaChamarValidadoresDeAdocaoAoSolicitar(){

        //ARRANGE
        this.dto = new SolicitacaoAdocaoDto(10L, 20L, "motivo qualquer");

        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        validacoes.add(validador1);
        validacoes.add(validador2);


        //ACT
        service.solicitar(dto);

        //ASSERT
        BDDMockito.then(validador1).should().validar(dto);
        BDDMockito.then(validador2).should().validar(dto);

    }

    @Test
    @DisplayName("Após solicitação, deveria enviar email para usuário")
    void deveriaEnviarEmailDeAdocaoAoSolicitar(){

        //ARRANGE
        this.dto = new SolicitacaoAdocaoDto(10L, 20L, "motivo qualquer");

        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);



        //ACT
        service.solicitar(dto);

        //ASSERT
        then(repository).should().save(adocaoCaptor.capture());
        Adocao adocaoSalve = adocaoCaptor.getValue();

        verify(emailService).enviarEmail(any(), any(), any());


    }

    // === MARCAR COMO APROVADA ===

}