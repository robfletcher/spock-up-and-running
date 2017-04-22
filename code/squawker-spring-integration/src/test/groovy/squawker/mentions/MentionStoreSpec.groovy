package squawker.mentions

import co.freeside.jdbi.time.TimeTypesArgumentFactory
import co.freeside.jdbi.time.TimeTypesMapperFactory
import com.oreilly.spock.TruncateTables
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import org.skife.jdbi.v2.tweak.HandleCallback
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.lang.Subject
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore
import static java.time.Instant.now

class MentionStoreSpec extends Specification implements TableHelper {

  @TruncateTables(DBIConnector)
  def dbi = new DBI("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false")
  @AutoCleanup Handle handle
  MessageStore messageStore
  UserStore userStore
  @Subject MentionStore mentionStore

  def setup() {
    dbi.registerArgumentFactory(new TimeTypesArgumentFactory())
    dbi.registerMapper(new TimeTypesMapperFactory())

    handle = dbi.open()

    userStore = handle.attach(UserStore)
    userStore.createUserTable()

    messageStore = handle.attach(MessageStore)
    messageStore.createMessageTable()

    mentionStore = handle.attach(MentionStore)
    mentionStore.createMentionTable()
  }

  def "ignores a second insert with the same user and message"() {
    given:
    def user = userStore.insert(postedBy)
    def message = messageStore.insert(user, messageText, now())

    and:
    userStore.insert(mentioned)

    and:
    mentionStore.insert(mentioned, message)

    when:
    mentionStore.insert(mentioned, message)

    then:
    count("mention") == 1

    where:
    postedBy = "kirk"
    mentioned = "spock"
    messageText = "Report, Mr @spock!"
  }

  def "ignores unknown usernames"() {
    given:
    def user = userStore.insert(postedBy)
    def message = messageStore.insert(user, messageText, now())

    expect:
    !userStore.find(mentioned)

    when:
    mentionStore.insert(mentioned, message)

    then:
    count("mention") == 0

    where:
    postedBy = "kirk"
    mentioned = "spock"
    messageText = "Report, Mr @spock!"
  }

  @Override
  def <R> R withHandle(HandleCallback<R> closure) {
    closure.withHandle(handle)
  }
}
