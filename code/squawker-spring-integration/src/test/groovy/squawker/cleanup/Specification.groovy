package squawker.cleanup

import groovy.transform.Memoized
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.tweak.HandleCallback
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Stepwise
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore
import squawker.mentions.MentionStore
import squawker.mentions.TableHelper
import static java.time.Instant.now

@Stepwise
abstract class Specification extends spock.lang.Specification implements TableHelper {

  // tag::autowired-dbi[]
  @Autowired DBI dbi
  // end::autowired-dbi[]
  @Autowired UserStore userStore
  @Autowired MessageStore messageStore
  @Autowired MentionStore mentionStore

  def "create some data"() {
    given:
    def user1 = userStore.insert("spock")
    def user2 = userStore.insert("kirk")

    and:
    messageStore.insert(user1, "Fascinating!", now())

    and:
    def message2 = messageStore.insert(user2, "Mr @spock, report!", now())

    and:
    mentionStore.insert("spock", message2)

    expect:
    count("user") == 2
    count("message") == 2
    count("mention") == 1
  }

  def "data should no longer exist"() {
    given:
    def handle = dbi.open()

    expect:
    tableNames().size() > 0

    and:
    tableNames().collectEntries {
      [(it): count(it)]
    } every {
      it.value == 0
    }

    cleanup:
    handle.close()
  }

  @Memoized
  List<String> tableNames() {
    withHandle { handle ->
      def tables = handle.connection.metaData.getTables(null, null, null, ["TABLE"] as String[])
      def tableNames = []
      while (tables.next()) {
        tableNames << tables.getString("TABLE_NAME")
      }
      return tableNames
    }
  }

  @Override
  <R> R withHandle(HandleCallback<R> closure) {
    dbi.withHandle(closure)
  }
}
