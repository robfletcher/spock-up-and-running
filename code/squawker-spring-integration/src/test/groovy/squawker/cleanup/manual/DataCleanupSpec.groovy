package squawker.cleanup.manual

import org.springframework.boot.test.context.SpringBootTest
import squawker.cleanup.Specification
import squawker.spring.Main

@SpringBootTest(classes = Main)
class DataCleanupSpec extends Specification {

  // tag::manual-cleanup[]
  def cleanup() {
    dbi.withHandle { handle ->
      handle.execute("delete from mention")
      handle.execute("delete from message")
      handle.execute("delete from user")
    }
  }
  // end::manual-cleanup[]

}
