package primer

import org.junit.Test

class Strings {

  @Test
  void templated() {
    // tag::templated[]
    def name = "Spock"
    def str = "Report, Mr ${name}"
    assert str == 'Report, Mr Spock'
    // end::templated[]
  }

  @Test
  void simple() {
    def name = "Spock"
    // tag::simple[]
    def str = "Report, Mr $name"
    assert str == 'Report, Mr Spock'
    // end::simple[]
  }

  @Test
  void propertyRef() {
    // tag::property-path[]
    def person = new Person(firstName: "Mr", lastName: "Spock")
    def str = "Report, $person.name"
    assert str == 'Report, Mr Spock'
    // end::property-path[]
  }

  @Test
  void methodCall() {
    def person = new Person(firstName: "Mr", lastName: "Spock")
    // tag::method-call[]
    def str = "Report, ${person.toString()}"
    assert str == 'Report, Mr Spock'
    // end::method-call[]
  }

  @Test
  void complex() {
    def person = new Person(firstName: "Mr", lastName: "Spock")
    // tag::complex[]
    def str = "Report, ${person.firstName + ' ' + person.lastName}"
    assert str == 'Report, Mr Spock'
    // end::complex[]
  }

  @Test
  void escaped() {
    // tag::escaped[]
    def str = "Report, Mr \$name"
    assert str == 'Report, Mr $name'
    // end::escaped[]
  }

  @Test
  void multiline() {
    def name = "Spock"
    // tag::multi-line[]
    def str = """
    $name: Fascinating!
    Kirk: Report, Mr $name!
    """
    // end::multi-line[]
      .stripIndent()
    // tag::multi-line[]
    assert str == '\nSpock: Fascinating!\nKirk: Report, Mr Spock!\n'
    // end::multi-line[]
  }

  @Test
  void indentMultiline() {
    // tag::escape-newline[]
    def s = """\
    Fascinating!"""
    // end::escape-newline[]
    .stripIndent()
    // tag::escape-newline[]
    assert s == "Fascinating!"
    // end::escape-newline[]
  }

  @Test
  void stripIndent() {
    // tag::strip-indent[]
    def s = """\
      Kirk: Report, Mr Spock!
      Spock: The lifeform is most unusual, Captain.
             I have never encountered anything like it.\
    """.stripIndent()
    assert s.startsWith("Kirk:")
    assert s.contains("\nSpock")
    // end::strip-indent[]
  }

}
