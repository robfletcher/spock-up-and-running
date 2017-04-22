package squawker.cleanup.truncate

import javax.sql.DataSource
import com.oreilly.spock.TruncateTables
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import squawker.cleanup.Specification
import squawker.spring.Main

@SpringBootTest(classes = Main)
class DataCleanupSpec extends Specification {

  // tag::truncate-tables[]
  @TruncateTables
  @Autowired DataSource dataSource
  // end::truncate-tables[]

}
