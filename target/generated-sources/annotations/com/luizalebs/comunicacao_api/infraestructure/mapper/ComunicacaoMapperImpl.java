package com.luizalebs.comunicacao_api.infraestructure.mapper;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-08T21:24:29-0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Azul Systems, Inc.)"
)
@Component
public class ComunicacaoMapperImpl implements ComunicacaoMapper {

    @Override
    public ComunicacaoEntity paraComunicacaoEntity(ComunicacaoInDTO comunicacaoInDTO) {
        if ( comunicacaoInDTO == null ) {
            return null;
        }

        ComunicacaoEntity comunicacaoEntity = new ComunicacaoEntity();

        return comunicacaoEntity;
    }

    @Override
    public ComunicacaoOutDTO paraComunicacaoOutDTO(ComunicacaoEntity comunicacaoEntity) {
        if ( comunicacaoEntity == null ) {
            return null;
        }

        ComunicacaoOutDTO comunicacaoOutDTO = new ComunicacaoOutDTO();

        return comunicacaoOutDTO;
    }
}
