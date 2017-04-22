package squawker.cleanup.extern

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import squawker.cleanup.Specification
import squawker.spring.Main
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD

// tag::sql-cleanup-script[]
@SpringBootTest(classes = Main)
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/cleanup.sql")
// end::sql-cleanup-script[]
class DataCleanupSpec extends Specification {
}
