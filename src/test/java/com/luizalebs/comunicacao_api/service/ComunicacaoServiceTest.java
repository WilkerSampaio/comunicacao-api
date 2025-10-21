package com.luizalebs.comunicacao_api.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTOFixture;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.infraestructure.client.EmailClient;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.exception.BadRequestException;
import com.luizalebs.comunicacao_api.infraestructure.exception.ResourceNotFoundException;
import com.luizalebs.comunicacao_api.infraestructure.mapper.ComunicacaoMapper;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComunicacaoServiceTest {

    @InjectMocks
    ComunicacaoService comunicacaoService;

    @Mock
    ComunicacaoRepository comunicacaoRepository;

    @Mock
    EmailClient emailClient;

    @Mock
    ComunicacaoMapper comunicacaoMapper;

    ComunicacaoEntity comunicacaoEntity;
    ComunicacaoOutDTO comunicacaoOutDTO;
    ComunicacaoInDTO comunicacaoInDTO;
    LocalDateTime dataHora;

    @BeforeEach
    void setup(){

        dataHora = LocalDateTime.of(2025, 6, 12, 12, 23,12);

        comunicacaoEntity = ComunicacaoEntity.builder()
                .id(1L)
                .dataHoraEnvio(dataHora)
                .nomeDestinatario("Usuario")
                .emailDestinatario("usuario@gmail.com")
                .telefoneDestinatario("6599202918")
                .mensagem("Olá, tudo bem ?")
                .modoDeEnvio(ModoEnvioEnum.EMAIL)
                .statusEnvio(StatusEnvioEnum.PENDENTE)
                .build();

        comunicacaoOutDTO = ComunicacaoOutDTOFixture.build(
                1L,
                dataHora,
                "Usuario",
                "usuario@gmail.com",
                "6599202918",
                "Olá, tudo bem ?",
                ModoEnvioEnum.EMAIL,
                StatusEnvioEnum.PENDENTE);

        comunicacaoInDTO = ComunicacaoInDTOFixture.build(dataHora,
                "Usuario",
                "usuario@gmail.com",
                "6599202918",
                "Olá, tudo bem ?",
                ModoEnvioEnum.EMAIL,
                StatusEnvioEnum.PENDENTE);
    }

    @Test
    void deveAgendarComunicacaoComSucesso(){
        when(comunicacaoMapper.paraComunicacaoEntity(comunicacaoInDTO)).thenReturn(comunicacaoEntity);
        when(comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);
        when(comunicacaoRepository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);

        ComunicacaoOutDTO response = comunicacaoService.agendarComunicacao(comunicacaoInDTO);

        assertEquals(comunicacaoOutDTO, response);

        verify(comunicacaoMapper).paraComunicacaoEntity(comunicacaoInDTO);
        verify(comunicacaoMapper).paraComunicacaoOutDTO(comunicacaoEntity);
        verify(comunicacaoRepository).save(comunicacaoEntity);

        verifyNoMoreInteractions(comunicacaoMapper, comunicacaoRepository);
    }

    @Test
    void naoDeveAgendarComunicacaoCasoDTONull(){

        BadRequestException e = assertThrows(BadRequestException.class, ()-> comunicacaoService.agendarComunicacao(null));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Dados de entrada inválidos."));

        verifyNoInteractions(comunicacaoMapper, comunicacaoRepository);
    }

    @Test
    void deveBuscarStatusComunicacaoComSucesso(){
        when(comunicacaoRepository.findByEmailDestinatario(comunicacaoInDTO.getEmailDestinatario())).thenReturn(Optional.of(comunicacaoEntity));
        when(comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);

         ComunicacaoOutDTO response = comunicacaoService.buscarStatusComunicacao(comunicacaoInDTO.getEmailDestinatario());

         assertEquals(comunicacaoOutDTO, response);

         verify(comunicacaoRepository).findByEmailDestinatario(comunicacaoInDTO.getEmailDestinatario());
         verify(comunicacaoMapper).paraComunicacaoOutDTO(comunicacaoEntity);

         verifyNoMoreInteractions(comunicacaoMapper, comunicacaoRepository);

    }

    @Test
    void naoDeveBuscarStatusComunicacaoComSuceso(){
       String emailInexistente = "naoexiste@gmail.com";
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, ()-> comunicacaoService.buscarStatusComunicacao(emailInexistente));


        assertThat(e.getMessage(), notNullValue());
        assertThat(e.getMessage(), is("Recurso não encontrado"));

        verify(comunicacaoRepository).findByEmailDestinatario(emailInexistente);
        verifyNoInteractions(comunicacaoMapper);
        verifyNoMoreInteractions(comunicacaoRepository,comunicacaoMapper);
    }

    @Test
    void deveAlterarStatusComunicacaoComSucesso(){

        when(comunicacaoRepository.findByEmailDestinatario(comunicacaoInDTO.getEmailDestinatario())).thenReturn(Optional.of(comunicacaoEntity));
        when(comunicacaoRepository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);

        ComunicacaoOutDTO response = comunicacaoService.alterarStatusComunicacao(comunicacaoInDTO.getEmailDestinatario());

        assertEquals(comunicacaoOutDTO, response);
        verify(comunicacaoRepository).findByEmailDestinatario(comunicacaoInDTO.getEmailDestinatario());
        verify(comunicacaoMapper).paraComunicacaoOutDTO(comunicacaoEntity);

        verifyNoMoreInteractions(comunicacaoRepository,comunicacaoMapper);

    }

    @Test
    void naoDeveAlterarStatusComunicacaoComSuceso(){
        String emailInexistente = "naoexiste@gmail.com";
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, ()-> comunicacaoService.alterarStatusComunicacao(emailInexistente));


        assertThat(e.getMessage(), notNullValue());
        assertThat(e.getMessage(), is("Recurso não encontrado"));

        verify(comunicacaoRepository).findByEmailDestinatario(emailInexistente);
        verifyNoInteractions(comunicacaoMapper);
        verifyNoMoreInteractions(comunicacaoRepository,comunicacaoMapper);
    }

    @Test
    void deveEnviarEmailComSucesso(){
        String respostaEsperada = "E-mail enviado com sucesso para " + comunicacaoOutDTO.getEmailDestinatario();

        when(emailClient.enviarEmail(comunicacaoInDTO)).thenReturn(respostaEsperada);

        String respostaAtual = comunicacaoService.enviarEmail(comunicacaoInDTO);

        assertEquals(respostaEsperada, respostaAtual);

        verify(emailClient).enviarEmail(comunicacaoInDTO);
        verifyNoMoreInteractions(emailClient);

    }


}
