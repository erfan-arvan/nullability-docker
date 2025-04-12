package com.namelessmc.java_api.exception;
public class CannotReportSelfException extends ApiErrorException {
	private static final long serialVersionUID = 1L;
	public CannotReportSelfException() {
		super(ApiError.CANNOT_REPORT_YOURSELF);
	}
}
