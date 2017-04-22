package squawker.schedule

import java.time.Clock
import com.oreilly.spock.TruncateTables
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.exceptions.DBIException
import org.skife.jdbi.v2.tweak.HandleCallback
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import squawker.jdbi.UserStore
import squawker.mentions.DBIConnector
import squawker.mentions.TableHelper
import squawker.schedule.ScheduledMessageStore.ScheduledMessageRequest
import squawker.spring.Main

@SpringBootTest(classes = Main)
class ScheduledMessageStoreSpec2 extends Specification implements TableHelper {

  @Shared Clock clock = Clock.systemDefaultZone()
  @Autowired @TruncateTables(DBIConnector) DBI dbi
  @Autowired UserStore userStore

  @Subject @Autowired ScheduledMessageStore scheduledMessageStore

  def "can schedule multiple messages"() {
    given:
    def user = userStore.insert(username)

    when:
    scheduledMessageStore.scheduleMessages(user, messages.collect {
      new ScheduledMessageRequest(it, clock.instant())
    })

    then:
    count("scheduled_message") == messages.size()

    where:
    username = "spock"
    messages = [
      "Fascinating!",
      "Insufficient facts always invite danger.",
      "Live long and prosper."
    ]
  }

  def "if one insert fails nothing is inserted"() {
    given:
    def user = userStore.insert(username)

    when:
    scheduledMessageStore.scheduleMessages(user, messages.collect {
      new ScheduledMessageRequest(it, clock.instant())
    })

    then:
    thrown DBIException

    and:
    count("scheduled_message") == 0

    where:
    username = "spock"
    validMessages = [
      "Fascinating!",
      "Insufficient facts always invite danger.",
      "Live long and prosper."
    ]
    invalidMessage = """Lieutenant, I am half Vulcanian. Vulcanians do not
      speculate. I speak from pure logic. If I let go of a hammer on a planet
      that has a positive gravity, I need not see it fall to know that it has in
      fact fallen."""
    messages = validMessages + invalidMessage
  }

  @Override
  def <R> R withHandle(HandleCallback<R> closure) {
    dbi.withHandle(closure)
  }
}
