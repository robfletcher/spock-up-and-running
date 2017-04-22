package primer

import org.junit.Test

class Spread {

  @Test
  void spreadCollect() {
    // tag::spread[]
    def crew = ["Kirk", "McCoy", "Spock"]
    crew*.toUpperCase() == ["KIRK", "MCCOY", "SPOCK"]
    // end::spread[]
  }

  @Test
  void spreadParams() {
    // tag::spread-params[]
    def crew = ["Kirk", "McCoy", "Spock"]
    def params = [0, "Picard"]
    crew.set(*params)
    assert crew == ["Picard", "McCoy", "Spock"]
    // end::spread-params[]
  }

  // tag::vararg-method[]
  int countParams(Object... params) {
    params.size()
  }
  // end::vararg-method[]

  @Test
  void spreadVararg() {
    // tag::spread-vararg[]
    def crew = ["Kirk", "McCoy", "Spock"]
    assert countParams(crew) == 1
    assert countParams(*crew) == 3
    // end::spread-vararg[]
  }

}
