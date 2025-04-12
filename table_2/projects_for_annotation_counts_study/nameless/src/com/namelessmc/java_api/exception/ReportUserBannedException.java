package com.namelessmc.java_api.exception;
public class ReportUserBannedException extends ApiErrorException {
	private static final long serialVersionUID = 1L;
	public ReportUserBannedException() {
		super(ApiError.USER_CREATING_REPORT_BANNED);
	}
}
