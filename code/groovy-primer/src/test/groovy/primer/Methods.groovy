package primer

import org.junit.Test

class Methods {

  @SuppressWarnings("GrMethodMayBeStatic")
  // tag::method-with-default-param[]
  String crewTitle(String name, String title = "Mr") {
    title + " " + name
  }
  // end::method-with-default-param[]

  @Test
  void callWithSpecifiedValue() {
    // tag::call-method-with-default[]
    assert crewTitle("Kirk", "Captain") == "Captain Kirk"
    assert crewTitle("McCoy", "Doctor") == "Doctor McCoy"
    // end::call-method-with-default[]
  }

  @Test
  void callWithDefaultValue() {
    // tag::call-method-with-default[]
    assert crewTitle("Spock") == "Mr Spock"
    // end::call-method-with-default[]
  }

}
