package primer

import org.junit.Test

class Lists {

  @Test
  void literal() {
    // tag::list-literal[]
    def crew = ["Kirk", "Spock", "Bones"]
    // end::list-literal[]

    assert crew instanceof ArrayList
    assert crew.size() == 3

    // tag::indexes[]
    assert crew[0] == "Kirk" // <1>
    def i = 1
    assert crew[i] == "Spock" // <2>
    assert crew[0..1] == ["Kirk", "Spock"] // <3>
    assert crew[1..-1] == ["Spock", "Bones"] // <4>
    assert crew[1..0] == ["Spock", "Kirk"] // <5>

    crew[2] = "McCoy" // <6>
    assert crew == ["Kirk", "Spock", "McCoy"]

    crew[3] = "Sulu" // <7>
    assert crew == ["Kirk", "Spock", "McCoy", "Sulu"]

    crew[5] = "Chekov" // <8>
    assert crew == ["Kirk", "Spock", "McCoy", "Sulu", null, "Chekov"]
    // end::indexes[]
  }

  @Test
  void subscripts() {
    // tag::list-subscript[]
    def list = ["a", "b", "c"]

    assert list[0] == "a" // <1>

    list[1] = "d" // <2>
    assert list.join("") == "adc"
    // end::list-subscript[]
  }

  @Test
  void appendOperator() {
    // tag::left-shift-append[]
    def list = []
    list << "a"
    list << "b" << "c"
    assert list == ["a", "b", "c"]
    // end::left-shift-append[]
  }

  @Test
  void immutable() {
    // tag::as-immutable[]
    def crew = ["Kirk", "Spock", "Bones"].asImmutable()
    // end::as-immutable[]
    try {
      crew << "Scotty"
      assert false : "Should have thrown UnsupportedOperationException"
    } catch (UnsupportedOperationException e) {

    } catch (e) {
      assert false : "Should have thrown UnsupportedOperationException"
    }
  }

  @Test
  void asSet() {
    // tag::as-set[]
    def crew = ["Kirk", "Spock", "Bones"] as Set
    assert crew instanceof HashSet
    // end::as-set[]
    crew << "Spock"
    assert crew.size() == 3
  }

  @Test
  void asArray() {
    // tag::as-array[]
    def crew = ["Kirk", "Spock", "Bones"] as String[]
    assert crew instanceof String[]
    // end::as-array[]
  }
}
