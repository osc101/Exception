package com.ldd600.exception.context;

public class CoreContextFactory {
	private static CoreContextFactory instance;

	private volatile ExceptionContext exceptionContext;

	private Object exceptionContextLock = new Object();

	private CoreContextFactory() {

	}

	public static synchronized CoreContextFactory getInstance() {
		if (null == instance) {
			instance = new CoreContextFactory();
		}
		return instance;
	}

	public ExceptionContext getExceptionContext() {
		ExceptionContext tempExpContext = exceptionContext;
		if (tempExpContext == null) { 
			synchronized (exceptionContextLock) {
				tempExpContext = exceptionContext;
				if (tempExpContext == null)
					exceptionContext = tempExpContext = new ExceptionContext();
			}
		}
		return tempExpContext;
	}
}
