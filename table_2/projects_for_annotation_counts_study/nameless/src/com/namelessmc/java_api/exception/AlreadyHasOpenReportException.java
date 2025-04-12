package com.namelessmc.java_api.exception;
public class AlreadyHasOpenReportException extends ApiErrorException {
	private static final long serialVersionUID = 1L;
	public AlreadyHasOpenReportException() {
		super(ApiError.USER_ALREADY_HAS_OPEN_REPORT);
	}
}
