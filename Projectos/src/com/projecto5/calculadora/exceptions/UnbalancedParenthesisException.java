package com.projecto5.calculadora.exceptions;

public class UnbalancedParenthesisException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;

	public UnbalancedParenthesisException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
