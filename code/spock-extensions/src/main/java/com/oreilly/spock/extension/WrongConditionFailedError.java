package com.oreilly.spock.extension;

import org.spockframework.runtime.ConditionNotSatisfiedError;
import org.spockframework.runtime.SpockAssertionError;

public class WrongConditionFailedError extends SpockAssertionError {
  private final String expected;
  private final ConditionNotSatisfiedError actual;

  public WrongConditionFailedError(String expected, ConditionNotSatisfiedError actual) {
    this.expected = expected;
    this.actual = actual;
  }

  @Override
  public String getMessage() {
    return String.format("Expected failure condition '%s', but got '%s'", expected, actual.getCondition().getRendering());
  }
}
