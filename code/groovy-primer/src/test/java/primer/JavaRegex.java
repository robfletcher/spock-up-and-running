package primer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

public class JavaRegex {

  @Test public void find() {
    Matcher matcher =
      // tag::java-match[]
      Pattern.compile("\\w+!").matcher("Fascinating!");
    // end::java-match[]
    assert matcher.find();
  }

  @Test
  public void pattern() {
    Pattern p =
      // tag::pattern[]
      Pattern.compile("\\w+");
      // end::pattern[]
    assert p instanceof Pattern;
  }
}
