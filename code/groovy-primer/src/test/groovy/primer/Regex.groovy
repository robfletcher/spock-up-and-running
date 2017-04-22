package primer

import java.util.regex.Pattern
import org.junit.Test

class Regex {

  @Test
  void match() {
    // tag::regex-literals[]
    assert "Fascinating!" ==~ /\w+!/
    assert "Report, Mr Spock!" ==~ /(\w+[,!]?\s*)+/
    // end::regex-literals[]
  }

  @Test
  void matchUrl() {
    // tag::match-url[]
    def url = "http://shop.oreilly.com/product/0636920038597.do"
    assert url ==~ $/https?://(\w+\.)+com(/\w+)*(\.\w+)?/$
    // end::match-url[]
  }

  @Test
  void partialMatch() {
    // tag::true-partial[]
    assert "Fascinating!" =~ /\w/
    // end::true-partial[]

    try {
      // tag::false-partial[]
      assert "Fascinating!" =~ /\d/
      // end::false-partial[]
      assert false
    } catch (AssertionError e) {
    }
  }

  @Test
  void completeMatch() {
    // tag::complete-match[]
    assert "Fascinating!" ==~ /\w+!/
    assert "Fascinating!" =~ /^\w+!$/
    // end::complete-match[]
  }

  @Test
  void pattern() {
    // tag::pattern[]
    def pattern = ~/\w+/
    assert pattern instanceof Pattern
    // end::pattern[]
  }
}
