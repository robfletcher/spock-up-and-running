package primer

import org.junit.Test

class Dispatch {

  void method(String s) {
    println "hi"
  }

  void method(Object o) {
    throw new UnsupportedOperationException()
  }

  @Test
  void dispatch() {
    def o = "Spock"
    method(o)
  }

}
