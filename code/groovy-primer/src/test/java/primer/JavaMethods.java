package primer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JavaMethods {

  // tag::java-parameter-default[]
  String crewTitle(String name, String title) {
    return title + " " + name;
  }

  String crewTitle(String name) {
    return crewTitle(name, "Mr");
  }
  // end::java-parameter-default[]

  @Test
  public void callWithSpecifiedValue() {
    assertEquals("Captain Kirk", crewTitle("Kirk", "Captain"));
    assertEquals("Doctor McCoy", crewTitle("McCoy", "Doctor"));
  }

  @Test
  public void callWithDefaultValue() {
    assertEquals("Mr Spock", crewTitle("Spock"));
  }
}
