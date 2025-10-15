package com.luizalebs.comunicacao_api.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.client.EmailClient;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.exception.BadRequestException;
import com.luizalebs.comunicacao_api.infraestructure.exception.ResourceNotFoundException;
import com.luizalebs.comunicacao_api.infraestructure.mapper.ComunicacaoMapper;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ComunicacaoService {

    private final ComunicacaoRepository repository;
    private final EmailClient emailClient;
    private final ComunicacaoMapper comunicacaoMapper;

    public ComunicacaoService(ComunicacaoRepository repository,EmailClient emailClient, ComunicacaoMapper comunicacaoMapper) {
        this.repository = repository;
        this.emailClient = emailClient;
        this.comunicacaoMapper = comunicacaoMapper;
    }

    public ComunicacaoOutDTO agendarComunicacao(ComunicacaoInDTO comunicacaoInDTO) {
        if (Objects.isNull(comunicacaoInDTO)) {
            throw new BadRequestException("Dados de entrada inválidos.");
        }
        comunicacaoInDTO.setStatusEnvio(StatusEnvioEnum.PENDENTE);
        ComunicacaoEntity comunicacaoEntity = comunicacaoMapper.paraComunicacaoEntity(comunicacaoInDTO);

        return comunicacaoMapper.paraComunicacaoOutDTO(repository.save(comunicacaoEntity));
    }

    public ComunicacaoOutDTO buscarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity comunicacaoEntity = repository.findByEmailDestinatario(emailDestinatario).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));

        return comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity);
    }

    public ComunicacaoOutDTO alterarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity comunicacaoEntity = repository.findByEmailDestinatario(emailDestinatario).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));

        comunicacaoEntity.setStatusEnvio(StatusEnvioEnum.CANCELADO);

        return comunicacaoMapper.paraComunicacaoOutDTO(repository.save(comunicacaoEntity));
    }

    public String enviarEmail(ComunicacaoInDTO comunicacaoInDTO){
          return emailClient.enviarEmail(comunicacaoInDTO);
    }

}
