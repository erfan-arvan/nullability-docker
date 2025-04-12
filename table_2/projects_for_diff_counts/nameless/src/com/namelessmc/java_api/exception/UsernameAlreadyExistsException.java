package com.namelessmc.java_api.exception;
public class UsernameAlreadyExistsException extends ApiErrorException {
	private static final long serialVersionUID = 1L;
	public UsernameAlreadyExistsException() {
		super(ApiError.USERNAME_ALREADY_EXISTS);
	}
}
