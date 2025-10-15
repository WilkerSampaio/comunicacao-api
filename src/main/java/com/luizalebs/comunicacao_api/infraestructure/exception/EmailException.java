package com.luizalebs.comunicacao_api.infraestructure.exception;

public class EmailException extends RuntimeException    {
    public EmailException(String message) {
        super(message);
    }
    public EmailException(String message, Throwable throwable){
        super(message, throwable);
    }

}
