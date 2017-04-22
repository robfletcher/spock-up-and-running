package primer

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import org.junit.Test

@CompileStatic
class TypeCheckedGenericClosureParams {

  @SuppressWarnings("GrMethodMayBeStatic")
  // tag::generic-closure-param[]
  <T> T transform(T s, @ClosureParams(FirstParam) Closure<T> transformer) {
    transformer(s)
  }
  // end::generic-closure-param[]

  @Test
  void passClosureToMethod() {
    def result = transform("Spock") {
      it.toUpperCase()
    }
    assert result == "SPOCK"
  }

  @SuppressWarnings(["GrMethodMayBeStatic", "UnnecessaryQualifiedReference"])
  // tag::generic-iterator[]
  <T> Iterable<T> transform(Iterable<T> s,
                            @ClosureParams(
                              FirstParam.FirstGenericType
                            ) Closure<T> transformer) {
    s.collect {
      transformer(it)
    }
  }
  // end::generic-iterator[]

  @Test
  void passClosureToIterator() {
    def result = transform(["Kirk", "Spock", "Scott"]) {
      it.toUpperCase()
    }
    assert result == ["KIRK", "SPOCK", "SCOTT"]
  }
}
