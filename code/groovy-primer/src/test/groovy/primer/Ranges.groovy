package primer

import org.junit.Test

class Ranges {

  @Test
  void intRanges() {
    // tag::int-range[]
    def ints = 1..5
    assert ints == [1, 2, 3, 4, 5]
    // end::int-range[]
  }

  @Test
  void halfExclusive() {
    // tag::half-exclusive[]
    def ints = 1..<5
    assert ints == [1, 2, 3, 4]
    // end::half-exclusive[]
  }

  @Test
  void slice() {
    // tag::string-slice[]
    def name = "James T Kirk"
    assert name[0..4] == "James"
    assert name[0..<5] == "James"
    assert name[8..-1] == "Kirk"
    // end::string-slice[]
  }
}
