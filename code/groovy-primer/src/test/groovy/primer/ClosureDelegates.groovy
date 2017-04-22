package primer

import groovy.transform.CompileStatic
import org.junit.Test

class ClosureDelegates {

  @CompileStatic
  @SuppressWarnings("GrMethodMayBeStatic")
  // tag::method-sets-delegate[]
  String transform(String s, Closure<String> transformer) {
    transformer.delegate = s
    transformer()
  }
  // end::method-sets-delegate[]

  @Test
  void delegate() {
    // tag::closure-with-delegate[]
    def result = transform("Spock") {
      toUpperCase()
    }
    // end::closure-with-delegate[]
    assert result == "SPOCK"
  }
}
