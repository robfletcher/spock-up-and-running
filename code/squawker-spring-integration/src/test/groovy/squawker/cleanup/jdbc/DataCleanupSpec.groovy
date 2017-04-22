package squawker.cleanup.jdbc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import squawker.cleanup.Specification
import squawker.spring.Main

@SpringBootTest(classes = Main)
class DataCleanupSpec extends Specification {

  // tag::jdbc-template[]
  @Autowired JdbcTemplate jdbcTemplate

  def cleanup() {
    jdbcTemplate.with {
      execute("delete from mention")
      execute("delete from message")
      execute("delete from user")
    }
  }
  // end::jdbc-template[]

}
