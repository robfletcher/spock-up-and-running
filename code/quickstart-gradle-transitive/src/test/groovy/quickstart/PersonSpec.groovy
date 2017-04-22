package quickstart

import spock.lang.Specification

class PersonSpec extends Specification {

  def "make sure Spock works"() {
    given:
    def person = new Person(firstName: "James", lastName: "Kirk")

    expect:
    person.firstName == "James"
    person.lastName == "Kirk"
  }

}
