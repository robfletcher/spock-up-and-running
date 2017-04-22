package primer

import org.junit.Test

class Truth {

  @Test void truthiness() {
    assert !null
    assert !""
    assert !0
    assert ![]
    assert ![:]
    assert !("Spock" =~ /Kirk/)
  }

}
