package primer

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.junit.Test

@CompileStatic
class TypeCheckedClosureParams {

  @SuppressWarnings("GrMethodMayBeStatic")
  // tag::static-closure-parameters[]
  String transform(String s,
                   @ClosureParams(
                     value = SimpleType,
                     options = "java.lang.String"
                   ) Closure<String> transformer) {
    transformer(s)
  }
  // end::static-closure-parameters[]

  @Test
  void passClosureToMethod() {
    def result = transform("Spock") {
      it.toUpperCase()
    }
    assert result == "SPOCK"
  }
}
