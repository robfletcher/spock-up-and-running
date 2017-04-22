package primer;

import org.junit.Test;

public class JavaDispatch {

  // tag::overloaded-methods[]
  void method(String s) {
    // end::overloaded-methods[]
    throw new UnsupportedOperationException();
    // tag::overloaded-methods[]
  }

  void method(Object o) {
  }
  // end::overloaded-methods[]

  @Test
  public void dispatch() {
    // tag::call[]
    Object o = "Spock";
    method(o);
    // end::call[]
  }

}
