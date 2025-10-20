package com.luizalebs.comunicacao_api.infrastructure.mapper;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTOFixture;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.mapper.ComunicacaoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ComunicacaoMapperTest {

    ComunicacaoMapper comunicacaoMapper;

    ComunicacaoEntity comunicacaoEntity;

    ComunicacaoOutDTO comunicacaoOutDTO;

    ComunicacaoInDTO comunicacaoInDTO;

    LocalDateTime dataHora;

    @BeforeEach
    void setup (){

        comunicacaoMapper = Mappers.getMapper(ComunicacaoMapper.class);

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
    void deveConverterParaEntityComSucesso(){

        ComunicacaoEntity entity = comunicacaoMapper.paraComunicacaoEntity(comunicacaoInDTO);

        assertEquals(comunicacaoInDTO.getDataHoraEnvio(), entity.getDataHoraEnvio());
        assertEquals(comunicacaoInDTO.getNomeDestinatario(), entity.getNomeDestinatario());
        assertEquals(comunicacaoInDTO.getEmailDestinatario(), entity.getEmailDestinatario());
        assertEquals(comunicacaoInDTO.getTelefoneDestinatario(), entity.getTelefoneDestinatario());
        assertEquals(comunicacaoInDTO.getMensagem(), entity.getMensagem());
        assertEquals(comunicacaoInDTO.getModoDeEnvio(), entity.getModoDeEnvio());
        assertEquals(comunicacaoInDTO.getStatusEnvio(), entity.getStatusEnvio());

    }

    @Test
    void deveConverterParaResponseComSucesso(){
        ComunicacaoOutDTO response = comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity);

        assertEquals(comunicacaoOutDTO, response);
    }



}
