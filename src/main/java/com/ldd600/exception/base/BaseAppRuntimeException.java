package com.ldd600.exception.base;

public class BaseAppRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -6021077900819863433L;

	private String errorCode;
	
    public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public BaseAppRuntimeException() {
		super();
	}
	
	public BaseAppRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseAppRuntimeException(String errorCode, String message, Throwable cause) {
		this(message, cause);
		setErrorCode(errorCode);
	}
	
	public BaseAppRuntimeException(String message) {
		super(message);
	}

	public BaseAppRuntimeException(Throwable cause) {
	    super(cause);
	}
	
	
	@Override
	public String toString() {
		String errorCode = getErrorCode();
		String s = (errorCode != null) ? errorCode + "--" + getClass().getName() : getClass().getName();
        String message = getLocalizedMessage();
        return (message != null) ? (s + ": " + message) : s;
	}
	

}
