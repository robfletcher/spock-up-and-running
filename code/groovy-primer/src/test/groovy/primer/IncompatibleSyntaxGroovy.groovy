package primer

import org.junit.Test

class IncompatibleSyntaxGroovy {

  @Test
  void multipleLoopVars() {
    // tag::multiple-loop-vars[]
    def seq = new StringBuilder()
    int j = 0
    for (int i = 0; i < 5; i++) {
      j += i
      seq.append(j).append(" ")
    }
    assert "0 1 3 6 10" == seq.toString().trim()
    // end::multiple-loop-vars[]
  }

  @Test
  void arrayInit() {
    // tag::array-init[]
    int[] array = [1, 2, 3] as int[]
    // end::array-init[]
    int[] array2 = [1, 2, 3] as int[]
    assert Arrays.equals(array, array2)
  }

  @Test
  void doLoop() {
    def text = new StringBuilder()
    // tag::do-loop[]
    while (text.length() < 80) {
      text.append(" ")
    }
    // end::do-loop[]
    assert text.length() == 80
  }

  @Test
  void withCloseable() {
    File file = File.createTempFile("test", ".txt")
    // tag::with-closeable[]
    new FileWriter(file).withCloseable { writer ->
      writer.append("Fascinating!")
    }
    // end::with-closeable[]

    assert "Fascinating!" == file.text
  }

  @Test
  void withWriter() {
    File file = File.createTempFile("test", ".txt")
    // tag::with-writer[]
    file.withWriter { writer ->
      writer.append("Fascinating!")
    }
    // end::with-writer[]

    assert "Fascinating!" == file.text
  }
}
