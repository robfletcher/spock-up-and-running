package squawker.mentions.tx.unit

import org.spockframework.mock.TooManyInvocationsError
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Subject
import squawker.User
import squawker.jdbi.MessageStore
import squawker.mentions.MentionStore
import squawker.mentions.MessageService

// tag::non-spring-test[]
class MessageServiceSpec extends Specification {

  def messageStore = Mock(MessageStore)
  def mentionStore = Mock(MentionStore)

  @Subject
  def messageService = new MessageService(messageStore, mentionStore)

// end::non-spring-test[]
  def "inserts a message with no mentions"() {
    when:
    messageService.postMessage(user, messageText)

    then:
    1 * messageStore.insert(user, messageText, _)
    0 * mentionStore._

    where:
    user = new User("spock")
    messageText = "Fascinating!"
  }

  // tag::non-spring-test[]
  def "inserts a mention if the text mentions another user"() {
    when:
    messageService.postMessage(user, messageText)

    then:
    1 * mentionStore.insert(mentionedUsername, _)

    where:
    user = new User("kirk")
    mentionedUsername = "spock"
    messageText = "Strike that from the record, Mr. @$mentionedUsername!"
  }

  // end::non-spring-test[]
  @FailsWith(TooManyInvocationsError)
  // tag::non-spring-test[]
  def "does not insert a mention if the text mentions a non-existent user"() {
    when:
    messageService.postMessage(user, messageText)

    then:
    0 * mentionStore.insert(mentionedUsername, _)

    where:
    user = new User("kirk")
    mentionedUsername = "spock"
    messageText = "Strike that from the record, Mr. @$mentionedUsername!"
  }

  // end::non-spring-test[]
  @FailsWith(TooManyInvocationsError)
  // tag::non-spring-test[]
  def "does not count multiple mentions in a single message"() {
    when:
    messageService.postMessage(user, messageText)

    then:
    1 * mentionStore.insert(mentionedUsername, _)

    where:
    user = new User("kirk")
    mentionedUsername = "spock"
    messageText = "Mr. @$mentionedUsername, come in! @$mentionedUsername, report!"
  }

  def "inserts multiple mentions if necessary"() {
    when:
    messageService.postMessage(user, messageText)

    then:
    1 * mentionStore.insert(mentionedUsernames[0], _)
    1 * mentionStore.insert(mentionedUsernames[1], _)

    where:
    user = new User("kirk")
    mentionedUsernames = ["spock", "bones"]
    messageText = "Mr @spock, @bones... to the transporter room."
  }
  // end::non-spring-test[]

  // tag::failed-tx-test[]
  def "if mention insert fails a message is not persisted"() {
    given:
    mentionStore.insert(*_) >> {
      throw new RuntimeException("test")
    }

    when:
    messageService.postMessage(user, messageText)

    then:
    def e = thrown(RuntimeException)
    e.message == "test"

    and:
    // ... what do we do here?

    where:
    user = new User("kirk")
    mentionedUsernames = ["spock", "bones"]
    messageText = "@${mentionedUsernames[0]}, @${mentionedUsernames[1]}" +
      " meet me in the transporter room!"
  }
  // end::failed-tx-test[]
// tag::non-spring-test[]
}
// end::non-spring-test[]
