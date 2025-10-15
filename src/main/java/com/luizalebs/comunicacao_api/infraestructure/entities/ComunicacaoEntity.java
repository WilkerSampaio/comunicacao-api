package com.luizalebs.comunicacao_api.infraestructure.entities;

import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "COMUNICACAO")
public class ComunicacaoEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dataHoraEnvio")
    private LocalDateTime dataHoraEnvio;

    @Column(name = "nomeDestinatario", nullable = false)
    private String nomeDestinatario;

    @Column(name = "emailDestinatario", nullable = false, unique = true)
    private String emailDestinatario;

    @Column(name = "telefoneDestinatario")
    private String telefoneDestinatario;

    @Column(name = "mensagem", nullable = false)
    private String mensagem;

    @Column(name = "modoDeEnvio")
    @Enumerated(EnumType.STRING)
    private ModoEnvioEnum modoDeEnvio;

    @Column(name = "statusEnvio")
    @Enumerated(EnumType.STRING)
    private StatusEnvioEnum statusEnvio;

}
