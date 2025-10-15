package com.luizalebs.comunicacao_api.infraestructure.repositories;

import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ComunicacaoRepository extends CrudRepository<ComunicacaoEntity, Long> {

   Optional<ComunicacaoEntity> findByEmailDestinatario(String nomeDestinatario);
}
