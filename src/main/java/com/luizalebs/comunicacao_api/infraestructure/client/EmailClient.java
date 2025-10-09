package com.luizalebs.comunicacao_api.infraestructure.client;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "Notificacao", url = "${notificacao.url}")
public interface EmailClient {

    @PostMapping
    String enviarEmail(ComunicacaoInDTO comunicacaoInDTO);
}
