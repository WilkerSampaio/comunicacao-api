package com.luizalebs.comunicacao_api.infraestructure.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeighConfig {

    @Bean
    public FeighError feighError(){
        return new FeighError();
    }
}
