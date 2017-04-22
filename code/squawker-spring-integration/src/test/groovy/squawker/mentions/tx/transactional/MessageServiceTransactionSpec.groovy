package squawker.mentions.tx.transactional

import com.oreilly.spock.FailsWithCondition
import org.spockframework.mock.MockUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import spock.mock.DetachedMockFactory
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore
import squawker.mentions.MentionStore
import squawker.mentions.MessageService
import squawker.mentions.tx.Specification
import squawker.spring.Main

// tag::nested-tx[]
@Transactional
@SpringBootTest(classes = Config)
class MessageServiceTransactionSpec extends Specification {

  @Autowired MessageService messageService
  @Autowired UserStore userStore
  @Autowired MessageStore messageStore
  @Autowired MentionStore mentionStore

  // end::nested-tx[]
  def "mention store is a mock"() {
    expect:
    new MockUtil().isMock(mentionStore)
  }

  @FailsWithCondition("""\
count("message") == 0
|                |
1                false
""")
  // tag::nested-tx[]
  def "if mention insert fails a message is not persisted"() {
    given:
    def user = userStore.insert(username)
    mentionedUsernames.each {
      userStore.insert(it)
    }

    and:
    mentionStore.insert(*_) >> {
      throw new RuntimeException("test")
    }

    when:
    messageService.postMessage(user, messageText)

    then:
    def e = thrown(RuntimeException)
    e.message == "test"

    and:
    count("message") == 0

    where:
    username = "kirk"
    mentionedUsernames = ["spock", "bones"]
    messageText = "@${mentionedUsernames[0]}, @${mentionedUsernames[1]}" +
      " meet me in the transporter room!"
  }

  @Import(Main)
  static class Config {
    def mockFactory = new DetachedMockFactory()

    @Bean
    MentionStore mentionStore() {
      mockFactory.Stub(MentionStore)
    }
  }
}
// end::nested-tx[]
