package primer

import org.junit.Test

class Properties {

  @Test
  void accessProperty() {
    // tag::access-property[]
    def person = new Person(firstName: "Leonard", lastName: "Nimoy")
    assert person.lastName == "Nimoy"
    // end::access-property[]
  }

  @Test
  void assignProperty() {
    // tag::assign-property[]
    def person = new Person(firstName: "James", lastName: "Kirk")
    person.firstName = "Jim"
    assert person.firstName == "Jim"
    // end::assign-property[]
  }

  @Test
  void fieldAccess() {
    // tag::field-access[]
    def person = new Person(firstName: "James", lastName: "Kirk")
    assert person.middleName == Optional.empty()
    assert person.@middleName == null

    person.middleName = "Tiberius"
    assert person.middleName == Optional.of("Tiberius")
    assert person.@middleName == "Tiberius"
    // end::field-access[]

    assert person.@middleName == "${person.middleName.get()}"
  }

}
