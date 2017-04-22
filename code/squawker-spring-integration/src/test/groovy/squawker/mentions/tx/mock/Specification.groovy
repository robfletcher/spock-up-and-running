package squawker.mentions.tx.mock

import squawker.jdbi.UserStore
import squawker.mentions.MentionStore
import squawker.mentions.MessageService
import static spock.util.Exceptions.getRootCause

abstract class Specification extends squawker.mentions.tx.Specification {

  // tag::error-from-stub[]
  def "if mention insert fails a message is not persisted"() {
    given:
    def user = userStore.insert(username)
    mentionedUsernames.each {
      userStore.insert(it)
    }

    and:
    mentionStore.insert(*_) >> {
      throw new RuntimeException("test") // <1>
    }

    when:
    messageService.postMessage(user, messageText)

    then:
    def e = thrown(RuntimeException)
    getRootCause(e).message == "test" // <2>

    and:
    count("message") == 0 // <3>

    where:
    username = "kirk"
    mentionedUsernames = ["spock", "bones"]
    messageText = "@${mentionedUsernames[0]}, @${mentionedUsernames[1]}" +
      " meet me in the transporter room!"
  }
  // end::error-from-stub[]

  abstract UserStore getUserStore()

  abstract MentionStore getMentionStore()

  abstract MessageService getMessageService()
}
