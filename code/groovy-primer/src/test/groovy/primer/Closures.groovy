package primer

import groovy.transform.CompileStatic
import org.junit.Test
import static java.util.stream.Collectors.toList

class Closures {

  @Test
  void simple() {
    // tag::simple-closure[]
    def closure = { String s ->
      s.toUpperCase()
    }
    assert closure("Spock") == "SPOCK"
    // end::simple-closure[]
  }

  @Test
  void typed() {
    // tag::untyped-closure[]
    def closure = { s ->
      s.toUpperCase()
    }
    assert closure("Spock") == "SPOCK"
    // end::untyped-closure[]
  }

  @Test
  void implicitParameter() {
    // tag::implicit-param[]
    def closure = {
      it.toUpperCase()
    }
    assert closure("Spock") == "SPOCK"
    // end::implicit-param[]
  }

  @Test
  void multipleParameters() {
    // tag::multi-params[]
    def closure = { first, last ->
      first + (last ? " " + last : "")
    }

    assert closure("James", "Kirk") == "James Kirk"
    assert closure("Spock", null) == "Spock"
    // end::multi-params[]
  }

  @Test
  void defaultParameters() {
    // tag::default-param[]
    def closure = { first, last = null ->
      first + (last ? " " + last : "")
    }

    assert closure("James", "Kirk") == "James Kirk"
    assert closure("Spock") == "Spock"
    // end::default-param[]
  }

  @Test
  void coerceToFunction() {
    // tag::coerce-to-sam[]
    def result = ["Kirk", "Spock", "Bones"]
      .stream()
      .map({ it.toUpperCase() })
      .collect(toList())
    assert result == ["KIRK", "SPOCK", "BONES"]
    // end::coerce-to-sam[]
  }

  @CompileStatic
  @SuppressWarnings("GrMethodMayBeStatic")
  // tag::function-accepting-closure[]
  String transform(String s, Closure<String> transformer) {
    transformer(s)
  }
  // end::function-accepting-closure[]

  @Test
  void passClosureToMethod() {
    // tag::pass-closure[]
    def result = transform("Spock") {
      it.toUpperCase()
    }
    // end::pass-closure[]
    assert result == "SPOCK"
  }

  @Test
  void methodRef() {
    // tag::method-ref[]
    def ref = "Spock".&contains
    assert ref instanceof Closure
    assert ref("k")
    assert !ref("x")
    assert ["S", "p", "o"].every(ref)
    // end::method-ref[]
  }

}
