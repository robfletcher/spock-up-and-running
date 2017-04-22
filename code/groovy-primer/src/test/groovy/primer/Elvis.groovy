package primer

import org.junit.Test

class Elvis {

  @Test void elvisNull() {
    // tag::elvis[]
    def a = "a"
    def b = "default"

    assert a != null ? a : b == "a"
    assert a ?: b == "a"

    a = null
    assert a != null ? a : b == "default"
    assert a ?: b == "default"
    // end::elvis[]
  }

}
