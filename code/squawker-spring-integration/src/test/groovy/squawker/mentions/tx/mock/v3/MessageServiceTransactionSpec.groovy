package squawker.mentions.tx.mock.v3

import org.spockframework.mock.MockUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import spock.mock.DetachedMockFactory
import squawker.jdbi.UserStore
import squawker.mentions.MentionStore
import squawker.mentions.MessageService
import squawker.mentions.tx.mock.Specification
import squawker.spring.Main

// tag::config-import-1[]
@SpringBootTest(classes = Config)
// end::config-import-1[]
class MessageServiceTransactionSpec extends Specification {

  @Autowired MessageService messageService
  @Autowired UserStore userStore
  @Autowired MentionStore mentionStore
  @Autowired ApplicationContext applicationContext

  def "it IS a mock"() {
    expect:
    applicationContext.getBeansOfType(MentionStore).size() == 1

    and:
    new MockUtil().isMock(mentionStore)
  }

  // tag::config-import-2[]
  @Import(Main)
  static class Config {
    private final DetachedMockFactory mockFactory = new DetachedMockFactory()

    @Bean
    MentionStore mentionStore() {
      mockFactory.Stub(MentionStore)
    }
  }
  // end::config-import-2[]
}
