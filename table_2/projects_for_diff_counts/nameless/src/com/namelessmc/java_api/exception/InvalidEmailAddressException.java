package com.namelessmc.java_api.exception;
public class InvalidEmailAddressException extends ApiErrorException {
	private static final long serialVersionUID = 1L;
	public InvalidEmailAddressException() {
		super(ApiError.INVALID_EMAIL_ADDRESS);
	}
}
