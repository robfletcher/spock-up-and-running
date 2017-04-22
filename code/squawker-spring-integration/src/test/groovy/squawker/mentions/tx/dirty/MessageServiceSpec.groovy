package squawker.mentions.tx.dirty

import com.oreilly.spock.TruncateTables
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.exceptions.DBIException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import spock.lang.Subject
import squawker.jdbi.UserStore
import squawker.mentions.DBIConnector
import squawker.mentions.MentionStore
import squawker.mentions.MessageService
import squawker.mentions.tx.Specification
import squawker.spring.Main

@SpringBootTest(classes = Main)
class MessageServiceSpec extends Specification {

  @Autowired @Subject MessageService messageService
  @Autowired @TruncateTables(DBIConnector) DBI dbi
  @Autowired UserStore userStore
  @Autowired MentionStore mentionStore

  // tag::dirties-context[]
  @DirtiesContext
  def "if mention insert fails a message is not persisted"() {
    given:
    def user = userStore.insert(username)
    mentionedUsernames.each {
      userStore.insert(it)
    }

    and:
    dbi.withHandle { handle ->
      handle.execute("drop table mention")
    }

    when:
    messageService.postMessage(user, messageText)

    then:
    thrown(DBIException)

    and:
    count("message") == 0

    where:
    username = "kirk"
    mentionedUsernames = ["spock", "bones"]
    messageText = "@${mentionedUsernames[0]}, @${mentionedUsernames[1]}" +
      " meet me in the transporter room!"
  }
  // end::dirties-context[]

  def "does not insert a mention if the text mentions a non-existent user"() {
    given:
    def user = userStore.insert(username)

    expect:
    !userStore.find(mentionedUsername)

    when:
    messageService.postMessage(user, messageText)

    then:
    count("mention") == 0

    where:
    username = "kirk"
    mentionedUsername = "spock"
    messageText = "Strike that from the record, Mr. @$mentionedUsername!"
  }

  def "does not count multiple mentions in a single message"() {
    given:
    def user = userStore.insert(username)
    userStore.insert(mentionedUsername)

    when:
    messageService.postMessage(user, messageText)

    then:
    count("mention") == 1

    where:
    username = "kirk"
    mentionedUsername = "spock"
    messageText = "Mr. @$mentionedUsername, come in! " +
      "@$mentionedUsername, report!"
  }
}
