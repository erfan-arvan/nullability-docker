package com.namelessmc.java_api.exception;
public class EmailAlreadyUsedException extends ApiErrorException {
	private static final long serialVersionUID = 1L;
	public EmailAlreadyUsedException() {
		super(ApiError.EMAIL_ALREADY_EXISTS);
	}
}
