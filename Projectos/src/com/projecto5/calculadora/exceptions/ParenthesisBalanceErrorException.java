package com.projecto5.calculadora.exceptions;

public class ParenthesisBalanceErrorException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;

	public ParenthesisBalanceErrorException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
