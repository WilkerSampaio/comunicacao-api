package com.luizalebs.comunicacao_api.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.luizalebs.comunicacao_api.api.controller.ComunicacaoController;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTOFixture;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.exception.EmailException;
import com.luizalebs.comunicacao_api.infraestructure.exception.GlobalExceptionHandler;
import com.luizalebs.comunicacao_api.infraestructure.exception.ResourceNotFoundException;
import com.luizalebs.comunicacao_api.service.ComunicacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ComunicacaoControllerTest {

    @InjectMocks
    ComunicacaoController comunicacaoController;

    @Mock
    ComunicacaoService comunicacaoService;

    ComunicacaoEntity comunicacaoEntity;

    ComunicacaoOutDTO comunicacaoOutDTO;

    ComunicacaoInDTO comunicacaoInDTO;

    ComunicacaoOutDTO comunicacaoCancelada;

    LocalDateTime dataHora;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private String url;
    private String json;

    @BeforeEach
    void setup () throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(comunicacaoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .alwaysDo(print())
                .build();

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

        comunicacaoCancelada = ComunicacaoOutDTOFixture.build(
                comunicacaoOutDTO.getId(),
                comunicacaoOutDTO.getDataHoraEnvio(),
                comunicacaoOutDTO.getNomeDestinatario(),
                comunicacaoOutDTO.getEmailDestinatario(),
                comunicacaoOutDTO.getTelefoneDestinatario(),
                comunicacaoOutDTO.getMensagem(),
                comunicacaoOutDTO.getModoDeEnvio(),
                StatusEnvioEnum.CANCELADO
        );


        json = objectMapper.writeValueAsString(comunicacaoInDTO);

    }

    @Test
    void deveAgendarMensagemComSucesso() throws Exception {
        url = "/comunicacao/agendar";

        when(comunicacaoService.agendarComunicacao(comunicacaoInDTO)).thenReturn(comunicacaoOutDTO);

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json)
    ).andExpect(status().isOk());

    verify(comunicacaoService).agendarComunicacao(comunicacaoInDTO);
    verifyNoMoreInteractions(comunicacaoService);

    }

    @Test
    void naoDeveAgendarCasoJsonNull() throws Exception {
        url = "/comunicacao/agendar";

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verifyNoMoreInteractions(comunicacaoService);

    }

    @Test
    void deveBuscarStatusDaMensagemComSucesso() throws Exception {
        when(comunicacaoService.buscarStatusComunicacao(comunicacaoInDTO.getEmailDestinatario())).thenReturn(comunicacaoOutDTO);
        url = "/comunicacao";

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("emailDestinatario", comunicacaoInDTO.getEmailDestinatario())
                .content(json)
        ).andExpect(status().isOk());

        verify(comunicacaoService).buscarStatusComunicacao(comunicacaoInDTO.getEmailDestinatario());
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void naoDeveBuscarStatusComunicacaoQuandoEmailNaoInformado() throws Exception {
        url = "/comunicacao";

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(comunicacaoService);
    }

    @Test
    void deveRetornar404AoBuscarStatusQuandoEmailNaoExisteNoBanco() throws Exception {
        url = "/comunicacao";
        String emailInexistente = "naoexiste@gmail.com";

        when(comunicacaoService.buscarStatusComunicacao(emailInexistente)).thenThrow(new ResourceNotFoundException("Recurso não encontrado"));

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("emailDestinatario", emailInexistente)
        ).andExpect(status().isNotFound());

        verify(comunicacaoService).buscarStatusComunicacao(emailInexistente);
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void deveAlterarStatusComunicacaoComSucesso() throws Exception {
        url = "/comunicacao/alterar";

        when(comunicacaoService.alterarStatusComunicacao(comunicacaoInDTO.getEmailDestinatario())).thenReturn(comunicacaoCancelada);

        mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("emailDestinatario", comunicacaoInDTO.getEmailDestinatario())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusEnvio").value("CANCELADO"));

        verify(comunicacaoService).alterarStatusComunicacao(comunicacaoInDTO.getEmailDestinatario());
        verifyNoMoreInteractions(comunicacaoService);

    }
    @Test
    void deveRetornar404AoAlterarStatusComunicacaoComEmailInexistente() throws Exception {
        url = "/comunicacao/alterar";
        String emailInexistente = "naoexiste@gmail.com";

        when(comunicacaoService.alterarStatusComunicacao(emailInexistente)).thenThrow(new ResourceNotFoundException("Recurso não encontrado"));

        mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("emailDestinatario", emailInexistente)
        ).andExpect(status().isNotFound());

        verify(comunicacaoService).alterarStatusComunicacao(emailInexistente);
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void naoDeveAlterarStatusComunicacaoQuandoEmailNaoInformado() throws Exception {
        url = "/comunicacao/alterar";

        mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(comunicacaoService);
    }


    @Test
    void deveEnviarMensagemComSucessoImediatamente() throws Exception {
        url = "/comunicacao/mensagem";
        String respostaEsperada = "E-mail enviado com sucesso para " + comunicacaoOutDTO.getEmailDestinatario();

        when(comunicacaoService.enviarEmail(comunicacaoInDTO)).thenReturn(respostaEsperada);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());

    }
    @Test
    void deveRetornar500QuandoEnvioDoEmailFalhar() throws Exception {
        url = "/comunicacao/mensagem";

        when(comunicacaoService.enviarEmail(comunicacaoInDTO))
                .thenThrow(new EmailException("Erro ao enviar o email"));

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Erro ao enviar o email"));

        verify(comunicacaoService).enviarEmail(comunicacaoInDTO);
    }



}
