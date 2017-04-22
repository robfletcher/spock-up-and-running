package primer;

import java.io.*;
import java.util.Arrays;
import org.junit.Test;

public class IncompatibleSyntax {

  @Test public void multipleLoopVars() {
    // tag::multiple-loop-vars[]
    StringBuilder seq = new StringBuilder();
    for (int i = 0, j = 0; i < 5; i++, j += i) {
      seq.append(j).append(" ");
    }
    assert "0 1 3 6 10".equals(seq.toString().trim());
    // end::multiple-loop-vars[]
  }

  @Test public void arrayInit() {
    // tag::array-init[]
    int[] array1 = {1, 2, 3};
    int[] array2 = new int[] {1, 2, 3};
    // end::array-init[]
    assert Arrays.equals(array1, array2);
  }

  @Test public void doLoop() {
    StringBuilder text = new StringBuilder();
    // tag::do-loop[]
    do {
      text.append(" ");
    } while (text.length() < 80);
    // end::do-loop[]
    assert text.length() == 80;
  }

  @Test public void tryWithResource() throws IOException {
    File file = File.createTempFile("test", ".txt");
    // tag::try-with-resource[]
    try (Writer writer = new FileWriter(file)) {
      writer.append("Fascinating!");
    }
    // end::try-with-resource[]

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      assert "Fascinating!".equals(reader.readLine());
    }
  }

}
