package com.luizalebs.comunicacao_api.infraestructure.client;


import com.luizalebs.comunicacao_api.infraestructure.exception.BussinessException;
import com.luizalebs.comunicacao_api.infraestructure.exception.EmailException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeighError implements ErrorDecoder {


    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()) {
            case 500:
                return new EmailException("Erro ao enviar o email");
            default:
                return new BussinessException("Erro inesperado");
        }

    }
}
