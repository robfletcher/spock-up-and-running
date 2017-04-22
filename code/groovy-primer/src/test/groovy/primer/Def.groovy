package primer

import org.junit.Test

class Def {

  @Test
  void dynamicType() {
    // tag::dynamic-type[]
    def x = "hi"
    assert x instanceof String

    x = 1
    assert x instanceof Number

    x++
    assert x == 2

    x += "0"
    assert x == "20"

    x = { -> x instanceof String }
    assert x instanceof Closure

    assert !x()
    // end::dynamic-type[]
  }

}
