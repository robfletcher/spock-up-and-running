package primer;

import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.Test;

public class MethodRefs {
  @Test public void methodRef() {
    // tag::java-method-ref[]
    Predicate<String> ref = "Spock"::contains;
    assert ref.test("k");
    assert !ref.test("x");
    assert Stream.of("S", "p", "o").allMatch(ref);
    // end::java-method-ref[]
  }

}
