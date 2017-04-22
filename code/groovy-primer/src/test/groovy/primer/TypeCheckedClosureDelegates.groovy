package primer

import groovy.transform.CompileStatic
import org.junit.Test

@CompileStatic
class TypeCheckedClosureDelegates {

  @SuppressWarnings("GrMethodMayBeStatic")
  // tag::delegate-type-info[]
  String transform(String s, @DelegatesTo(String) Closure<String> transformer) {
    transformer.delegate = s
    transformer()
  }
  // end::delegate-type-info[]

  @Test
  void delegate() {
    def result = transform("Spock") {
      toUpperCase()
    }
    assert result == "SPOCK"
  }
}
