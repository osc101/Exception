package com.ldd600.exception.test;

import com.ldd600.exception.annotation.Exceptional;
import com.ldd600.exception.base.BaseAppException;
import com.ldd600.exception.base.handler.ConsoleHandler;

@Exceptional(errorCode="LDD600-00003", handlers={ConsoleHandler.class})
public class ExceptionFaker extends BaseAppException {

}
