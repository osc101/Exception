package com.ldd600.exception.test;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.springframework.beans.factory.BeanFactory;

import com.ldd600.exception.action.BusinessAction;
import com.ldd600.exception.base.BaseAppException;
import com.ldd600.exception.base.BaseAppRuntimeException;
import com.ldd600.exception.base.ConfigException;
import com.ldd600.exception.base.handler.ConsoleHandler;
import com.ldd600.exception.context.CoreContextFactory;
import com.ldd600.exception.dto.DefaultRequest;
import com.ldd600.exception.dto.DefaultResponse;
import com.ldd600.exception.dto.Request;
import com.ldd600.exception.dto.Response;
import com.ldd600.exception.webservice.ActionBrokerImpl;

public class ExceptionTest extends DependencyInjectionExceptionTestCase {
	Mockery context = new Mockery();
	ActionBrokerImpl broker = new ActionBrokerImpl();
	final Request request = new DefaultRequest();
	final Response response = new DefaultResponse();

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "applicationContext.xml" };
	}

	public void testExceptionThrow() {
		final BusinessAction<Response, Request> action = context
				.mock(BusinessAction.class);
		final BeanFactory beanFactory = context.mock(BeanFactory.class);
		assertThrowing(new Closure() {
			public void run() throws Throwable {
				context.checking(new Expectations() {
					{
						allowing(beanFactory).getBean("action");
						will(returnValue(action));
						one(action).execute(request, response);
						will(throwException(new BaseAppException()));
					}
				});
				broker.setExceptionHandler(new ConsoleHandler());
				broker.setBeanFactory(beanFactory);
				broker.execute("action", request, response);
			}

		}, BaseAppException.class);
	}

	public void testExceptionalAutoLoad() throws BaseAppException {
		final BeanFactory beanFactory = context.mock(BeanFactory.class);
		final BusinessAction<Response, Request> action = context
				.mock(BusinessAction.class);
		context.checking(new Expectations() {
			{
				allowing(beanFactory).getBean("action");
				will(returnValue(action));
				one(action).execute(request, response);
				will(throwException(new ConfigException()));
			}
		});
		broker.setBeanFactory(beanFactory);
		broker.execute("action", request, response);
		assertEquals(CoreContextFactory.getInstance().getExceptionContext()
				.getErrorCode(ConfigException.class), "LDD600-00002");
		context.assertIsSatisfied();
	}

	public void testRuntimeException() {
		final BusinessAction<Response, Request> action = context
				.mock(BusinessAction.class);
		final BeanFactory beanFactory = context.mock(BeanFactory.class);
		assertThrowing(new Closure() {
			public void run() throws Throwable {
				context.checking(new Expectations() {
					{
						allowing(beanFactory).getBean("action");
						will(returnValue(action));
						one(action).execute(request, response);
						will(throwException(new BaseAppRuntimeException()));
					}
				});
				broker.setExceptionHandler(new ConsoleHandler());
				broker.setBeanFactory(beanFactory);
				broker.execute("action", request, response);
			}

		}, BaseAppRuntimeException.class);
		// test config
		assertEquals(CoreContextFactory.getInstance().getExceptionContext()
				.getErrorCode(BaseAppRuntimeException.class), "LDD600-00001");
		// test handler
		assertFalse(response.isSuccess());
		assertEquals(response.getErrorCode(), CoreContextFactory.getInstance()
				.getExceptionContext().getErrorCode(
						BaseAppRuntimeException.class));
		context.assertIsSatisfied();
	}

	public void testCheckedException() {
		final BusinessAction<Response, Request> action = context
				.mock(BusinessAction.class);
		final BeanFactory beanFactory = context.mock(BeanFactory.class);
		assertThrowing(new Closure() {
			public void run() throws Throwable {
				context.checking(new Expectations() {
					{
						allowing(beanFactory).getBean("action");
						will(returnValue(action));
						one(action).execute(request, response);
						will(throwException(new ExceptionFaker()));
					}
				});
				broker.setBeanFactory(beanFactory);
				broker.execute("action", request, response);
			}

		}, ExceptionFaker.class);
		// test config
		assertEquals(CoreContextFactory.getInstance().getExceptionContext()
				.getErrorCode(ExceptionFaker.class), "LDD600-00003");
		// test handler
		assertFalse(response.isSuccess());
		assertEquals(response.getErrorCode(), CoreContextFactory.getInstance()
				.getExceptionContext().getErrorCode(
						ExceptionFaker.class));
		context.assertIsSatisfied();
	}
}
