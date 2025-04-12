package com.namelessmc.java_api.exception;
public class InvalidValidateCodeException extends ApiErrorException {
	private static final long serialVersionUID = 1L;
	public InvalidValidateCodeException() {
		super(ApiError.INVALID_VALIDATE_CODE);
	}
}
