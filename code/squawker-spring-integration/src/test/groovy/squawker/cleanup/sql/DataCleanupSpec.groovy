package squawker.cleanup.sql

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import squawker.cleanup.Specification
import squawker.spring.Main
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD

// tag::sql-cleanup[]
@SpringBootTest(classes = Main)
@Sql(executionPhase = AFTER_TEST_METHOD, statements = [
  "delete from mention",
  "delete from message",
  "delete from user"
])
// end::sql-cleanup[]
class DataCleanupSpec extends Specification {
}
