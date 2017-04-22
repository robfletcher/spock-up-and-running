package primer

import org.junit.Test

class Comparables {

  static
  // tag::comparable-class[]
  class Person implements Comparable<Person> {
    String firstName
    String lastName

    @Override
    int compareTo(Person o) {
      lastName <=> o.lastName ?: firstName <=> o.firstName
    }
  }
  // end::comparable-class[]

  @Test void spaceship() {
    // tag::use-spaceship[]
    def kirk = new Person(firstName: "James", lastName: "Kirk")
    def scotty = new Person(firstName: "Montgomery", lastName: "Scott")

    assert kirk < scotty

    def beverley = new Person(firstName: "Beverley", lastName: "Crusher")
    def wesley = new Person(firstName: "Wesley", lastName: "Crusher")

    assert wesley > beverley
    // end::use-spaceship[]
  }
}
