package squawker.mentions.tx.transactional

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore
import squawker.mentions.MentionStore
import squawker.mentions.MessageService
import squawker.mentions.tx.Specification
import squawker.spring.Main

// tag::transactional-test[]
@Transactional
@SpringBootTest(classes = Main)
class MessageServiceSpec extends Specification {

  @Autowired MessageService messageService
  @Autowired UserStore userStore
  @Autowired MessageStore messageStore
  @Autowired MentionStore mentionStore

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
// end::transactional-test[]
