package com.namelessmc.java_api;
public class NamelessException extends Exception {
	private static final long serialVersionUID = -3698433855091611529L;
	public NamelessException(final  String message) {
		super(message);
	}
	public NamelessException(final  String message, final  Throwable cause) {
		super(message, cause);
	}
	public NamelessException(final  Throwable cause) {
		super(cause);
	}
	public NamelessException() {
		super();
	}
}
