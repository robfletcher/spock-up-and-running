package primer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class JavaLists {

  @Test
  public void listLiteralEquivalent() {
    // tag::java-list[]
    List<String> crew = new ArrayList<>();
    crew.add("Kirk");
    crew.add("Spock");
    crew.add("Bones");
    // end::java-list[]

    assert crew.size() == 3;
  }

  @Test
  public void immutable() {
    // tag::as-immutable[]
    List<String> crew = new ArrayList<>();
    crew.add("Kirk");
    crew.add("Spock");
    crew.add("Bones");
    crew = Collections.unmodifiableList(crew);
    // end::as-immutable[]
    try {
      crew.add("Scotty");
      assert false : "Should have thrown UnsupportedOperationException";
    } catch (UnsupportedOperationException e) {

    } catch (Throwable e) {
      assert false : "Should have thrown UnsupportedOperationException";
    }
  }
}
