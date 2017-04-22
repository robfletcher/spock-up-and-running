package squawker.schedule

import java.time.Clock
import co.freeside.jdbi.time.TimeTypesArgumentFactory
import co.freeside.jdbi.time.TimeTypesMapperFactory
import com.oreilly.spock.FailsWithCondition
import com.oreilly.spock.TruncateTables
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.exceptions.DBIException
import org.skife.jdbi.v2.tweak.HandleCallback
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import squawker.Message
import squawker.jdbi.UserStore
import squawker.mentions.DBIConnector
import squawker.mentions.TableHelper
import squawker.schedule.ScheduledMessageStore.ScheduledMessageRequest

class ScheduledMessageStoreSpec extends Specification implements TableHelper {

  @Shared Clock clock = Clock.systemDefaultZone()
  @TruncateTables(DBIConnector)
  DBI dbi = new DBI("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false")
  UserStore userStore
  @Subject ScheduledMessageStore scheduledMessageStore

  def setup() {
    dbi.registerArgumentFactory(new TimeTypesArgumentFactory())
    dbi.registerMapper(new TimeTypesMapperFactory())

    userStore = dbi.open(UserStore)
    userStore.createUserTable()
    scheduledMessageStore = dbi.open(ScheduledMessageStore)
    scheduledMessageStore.createScheduledMessageTable()
  }

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

  @FailsWithCondition("""\
count("scheduled_message") == 0
|                          |
3                          false
""")
  def "if one insert fails nothing is inserted"() {
    given:
    def user = userStore.insert(username)

    expect:
    invalidMessage.length() > Message.MAX_TEXT_LENGTH

    when:
    scheduledMessageStore.scheduleMessages(user, (validMessages + invalidMessage).collect {
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
  }

  @Override
  def <R> R withHandle(HandleCallback<R> closure) {
    dbi.withHandle(closure)
  }
}
