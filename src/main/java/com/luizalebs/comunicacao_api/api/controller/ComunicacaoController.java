package com.luizalebs.comunicacao_api.api.controller;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.business.service.ComunicacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comunicacao")
@Tag(name = "Comunicação", description = "Agendamento de mensagens")
public class ComunicacaoController {

    private final ComunicacaoService service;

    public ComunicacaoController(ComunicacaoService service) {
        this.service = service;
    }

    @Operation(summary = "Agenda mensagem", description = "Programa uma mensagem para ser enviada ao destinatário")
    @PostMapping("/agendar")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> agendar(@RequestBody ComunicacaoInDTO dto)  {
        return ResponseEntity.ok(service.agendarComunicacao(dto));
    }

    @Operation(summary = "Dados da Mensagem", description = "Retornar todos os dados da comunicação")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    @GetMapping()
    public ResponseEntity<ComunicacaoOutDTO> buscarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.buscarStatusComunicacao(emailDestinatario));
    }

    @Operation(summary = "Altera Status", description = "Altera o status da mensagem para cancelado")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    @PatchMapping("/cancelar")
    public ResponseEntity<ComunicacaoOutDTO> cancelarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.alterarStatusComunicacao(emailDestinatario));
    }

    @PostMapping("/mensagem")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Void> enviarMensagem(@RequestBody ComunicacaoInDTO comunicacaoInDTO){
        service.enviarEmail(comunicacaoInDTO);
        return ResponseEntity.ok().build();
    }
}
