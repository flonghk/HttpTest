package com.zf.util;

public class DefinedException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private Log log = new Log(this.getClass());
	
	public DefinedException(String message, Throwable cause) {
		super(message, cause);
		log.info(message);
	}
	
	public DefinedException() {
		super();
	}

	public DefinedException(String message) {
		super(message);
		log.info(message);
	}

	public DefinedException(Throwable cause) {
		super(cause);
		log.info(cause.getCause().toString());
	}
	
	
	
}
