package com.ldd600.exception.base;

public class WrapperredAppException extends BaseAppRuntimeException {

	private static final long serialVersionUID = 1325601692472497460L;

	public WrapperredAppException(Exception e) {
		super(e);
	}
}
