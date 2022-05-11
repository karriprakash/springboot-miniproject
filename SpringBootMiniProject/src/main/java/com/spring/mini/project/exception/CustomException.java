package com.spring.mini.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.GATEWAY_TIMEOUT)
public class CustomException extends RuntimeException{

	
	private static final long serialVersionUID = 1L;
	public CustomException() {
		super();
	}
	public CustomException(String message) {
		super(message);
	}
}
