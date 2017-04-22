package primer

import org.junit.Test

class Iterators {

  @Test
  void each() {
    // tag::each[]
    def result = new StringBuilder()
    ["Kirk", "Spock", "McCoy"].each {
      result << it << ", "
    }
    assert result.toString() == "Kirk, Spock, McCoy, "
    // end::each[]
  }

  @Test
  void eachWithIndex() {
    // tag::each-with-index[]
    def result = new StringBuilder()
    ["Kirk", "Spock", "McCoy"].eachWithIndex { name, i ->
      if (i > 0) {
        result << ", "
      }
      result << i + 1 << ": " << name
    }
    assert result.toString() == "1: Kirk, 2: Spock, 3: McCoy"
    // end::each-with-index[]
  }

  @Test
  void find() {
    // tag::find[]
    def result = ["Kirk", "Spock", "McCoy"].find {
      it.contains("o")
    }
    assert result == "Spock"
    // end::find[]
  }

  @Test
  void findAll() {
    // tag::find-all[]
    def result = ["Kirk", "Spock", "McCoy"].findAll {
      it.contains("o")
    }
    assert result == ["Spock", "McCoy"]
    // end::find-all[]
  }

  @Test
  void collect() {
    // tag::collect[]
    def crew = [
      [firstName: "James", lastName: "Kirk"],
      [firstName: "Montgomery", lastName: "Scott"],
      [firstName: "Hikari", lastName: "Sulu"]
    ]
    assert crew.collect { it.lastName } == ["Kirk", "Scott", "Sulu"]
    // end::collect[]
  }

  @Test
  void implicitCollect() {
    def crew = [
      [firstName: "James", lastName: "Kirk"],
      [firstName: "Montgomery", lastName: "Scott"],
      [firstName: "Hikari", lastName: "Sulu"]
    ]
    // tag::implicit-collect[]
    assert crew.lastName == ["Kirk", "Scott", "Sulu"]
    // end::implicit-collect[]
  }

  @Test
  void spreadCollect() {
    def crew = [
      [firstName: "James", lastName: "Kirk"],
      [firstName: "Montgomery", lastName: "Scott"],
      [firstName: "Hikari", lastName: "Sulu"]
    ]
    // tag::spread-collect[]
    assert crew.lastName*.toUpperCase() == ["KIRK", "SCOTT", "SULU"]
    // end::spread-collect[]
  }

  @Test
  void any() {
    // tag::any[]
    assert ["Kirk", "Spock", "McCoy"].any {
      it.contains("o")
    }
    // end::any[]
  }

  @Test
  void every() {
    // tag::every[]
    assert ["Kirk", "Spock", "McCoy"].every {
      it.contains("c") || it.contains("k")
    }
    // end::every[]
  }

  @Test
  void chained() {
    // tag::chained[]
    def crew = [
        [name: "Crusher", active: true, dateOfBirth: "2348-01-01"],
        [name: "Kirk", active: false, dateOfBirth: "2233-03-22"],
        [name: "McCoy", active: false, dateOfBirth: "2227-01-01"],
        [name: "Picard", active: true, dateOfBirth: "2305-07-13"],
        [name: "Spock", active: false, dateOfBirth: "2230-01-06"],
        [name: "Worf", active: true, dateOfBirth: "2340-01-01"]
    ]

    def result = crew.findAll {
      it.active
    } sort { a, b ->
      a.dateOfBirth <=> b.dateOfBirth
    } collect {
      it.name
    } join(", ")

    assert result == "Picard, Worf, Crusher"
    // end::chained[]
  }
}
