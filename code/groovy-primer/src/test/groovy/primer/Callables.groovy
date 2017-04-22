package primer

import org.junit.Test

class Callables {

  static
  // tag::callable-class[]
  class Greeter {
    def call() {
      "Ahoy!"
    }
  }
  // end::callable-class[]

  @Test
  void callable() {
    // tag::call[]
    def greeter = new Greeter()
    assert greeter.call() == "Ahoy!"
    assert greeter() == "Ahoy!"
    // end::call[]
  }

  static
  // tag::callable-varargs[]
  class Greeter2 {
    def call(String... names) {
      names.collect { "Ahoy $it!" } join("\n")
    }
  }
  // end::callable-varargs[]

  @Test
  void callableVarargs() {
    // tag::call-with-args[]
    def greeter = new Greeter2()
    assert greeter("Spock", "Kirk", "Bones") == """\
    Ahoy Spock!
    Ahoy Kirk!
    Ahoy Bones!"""
    // end::call-with-args[]
    .stripIndent()
  }
}
