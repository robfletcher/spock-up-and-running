package com.oreilly.spock.extension;

import com.oreilly.spock.FailsWithCondition;
import org.spockframework.runtime.ConditionNotSatisfiedError;
import org.spockframework.runtime.WrongExceptionThrownError;
import org.spockframework.runtime.extension.IMethodInterceptor;
import org.spockframework.runtime.extension.IMethodInvocation;

public class FailsWithConditionInterceptor implements IMethodInterceptor {

  private final FailsWithCondition failsWithCondition;

  public FailsWithConditionInterceptor(FailsWithCondition failsWithCondition) {
    this.failsWithCondition = failsWithCondition;
  }

  @Override
  public void intercept(IMethodInvocation invocation) throws Throwable {
    try {
      invocation.proceed();
    } catch (Throwable t) {
      if (!ConditionNotSatisfiedError.class.isInstance(t)) {
        throw new WrongExceptionThrownError(ConditionNotSatisfiedError.class, t);
      } else if (!((ConditionNotSatisfiedError) t).getCondition().getRendering().equals(failsWithCondition.value())) {
        throw new WrongConditionFailedError(failsWithCondition.value(), (ConditionNotSatisfiedError) t);
      }
      return;
    }

    throw new WrongExceptionThrownError(ConditionNotSatisfiedError.class, null);
  }

}
