package primer

import org.junit.Test

class Appendables {

  @Test void appendToStream() {
    // tag::left-shift-append[]
    def buffer = new StringBuilder()
    buffer << "a" << "," << "b" << "," << "c"
    assert buffer.toString() == "a,b,c"
    // end::left-shift-append[]
  }

}
