package squawker.mentions.tx.mock.v2

import org.spockframework.mock.MockUtil
import org.spockframework.spring.xml.SpockMockFactoryBean
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import spock.lang.IgnoreRest
import squawker.jdbi.UserStore
import squawker.mentions.MentionStore
import squawker.mentions.MessageService
import squawker.mentions.tx.mock.Specification
import squawker.spring.Main

// tag::reversed-config[]
@SpringBootTest(classes = [Config, Main])
// end::reversed-config[]
class MessageServiceTransactionSpec extends Specification {

  @Autowired MessageService messageService
  @Autowired UserStore userStore
  @Autowired MentionStore mentionStore

  @IgnoreRest
  def "it's not a mock"() {
    expect:
    !new MockUtil().isMock(mentionStore)
  }

  static class Config {
    @Bean
    FactoryBean<MentionStore> mentionStore() {
      new SpockMockFactoryBean(MentionStore)
    }
  }
}
