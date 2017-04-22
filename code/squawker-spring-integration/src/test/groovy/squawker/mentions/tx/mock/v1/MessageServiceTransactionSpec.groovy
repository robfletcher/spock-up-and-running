package squawker.mentions.tx.mock.v1

import org.spockframework.mock.MockUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import spock.mock.DetachedMockFactory
import squawker.jdbi.UserStore
import squawker.mentions.MentionStore
import squawker.mentions.MessageService
import squawker.mentions.tx.mock.Specification
import squawker.spring.Main

// tag::config-order[]
@SpringBootTest(classes = [Main, Config])
// end::config-order[]
class MessageServiceTransactionSpec extends Specification {

  @Autowired MessageService messageService
  @Autowired UserStore userStore
  @Autowired MentionStore mentionStore

  // tag::assert-is-mock[]
  def "mock beans are auto-wired"() {
    expect:
    new MockUtil().isMock(mentionStore)
  }
  // end::assert-is-mock[]

  // tag::stub-config[]
  static class Config {
    private final DetachedMockFactory mockFactory = new DetachedMockFactory()

    @Bean
    MentionStore mentionStore() {
      mockFactory.Stub(MentionStore)
    }
  }
  // end::stub-config[]
}
