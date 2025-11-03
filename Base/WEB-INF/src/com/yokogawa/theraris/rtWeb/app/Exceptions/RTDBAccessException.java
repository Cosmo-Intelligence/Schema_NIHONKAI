package com.yokogawa.theraris.rtWeb.app.Exceptions;

public class RTDBAccessException extends Exception {
	public RTDBAccessException() {
		super();
	}

	public RTDBAccessException(String msg) {
		super(msg);
	}

	public RTDBAccessException(Exception e) {
		super(e);
	}

	public RTDBAccessException(String msg, Exception e) {
		super(msg, e);
	}
}
