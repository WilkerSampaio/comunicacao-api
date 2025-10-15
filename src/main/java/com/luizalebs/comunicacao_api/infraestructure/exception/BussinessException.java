package com.luizalebs.comunicacao_api.infraestructure.exception;

public class BussinessException extends RuntimeException {
  public BussinessException(String message) {
    super(message);
  }
}
