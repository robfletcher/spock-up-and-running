package squawker.mentions.fixtures.v4

import com.oreilly.spock.TruncateTables
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.tweak.HandleCallback
import org.spockframework.runtime.ConditionNotSatisfiedError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll
import squawker.jdbi.UserStore
import squawker.mentions.DBIConnector
import squawker.mentions.TableHelper
import squawker.spring.Main

// tag::active-profiles[]
@SpringBootTest(classes = Main)
@ActiveProfiles("integration")
// end::active-profiles[]
@Unroll
class MessageServiceSpec extends Specification implements TableHelper {

  @Autowired @TruncateTables(DBIConnector) DBI dbi
  @Autowired UserStore userStore

  def "#n users were created"() {
    expect:
    count("user") == n

    where:
    n = 3
  }

  @FailsWith(ConditionNotSatisfiedError)
  def "user @#user exists"() {
    expect:
    userStore.find(user)

    where:
    user << ["kirk", "spock", "bones"]
  }

  @FailsWith(ConditionNotSatisfiedError)
  def "user @#user follows @#captain"() {
    expect:
    userStore.find(user).follows(userStore.find(captain))

    where:
    user << ["spock", "bones"]
    captain = "kirk"
  }

  @Override
  <R> R withHandle(HandleCallback<R> closure) {
    dbi.withHandle(closure)
  }
}
