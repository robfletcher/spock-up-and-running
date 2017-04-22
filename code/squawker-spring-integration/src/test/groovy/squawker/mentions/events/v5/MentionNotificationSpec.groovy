package squawker.mentions.events.v5

import org.spockframework.mock.MockUtil
import org.spockframework.spring.xml.SpockMockFactoryBean
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import squawker.mentions.events.MentionEvent
import squawker.mentions.events.Specification
import squawker.spring.Main

@SpringBootTest(classes = [Main, Config])
class MentionNotificationSpec extends Specification {

  @Autowired ApplicationListener<MentionEvent> mentionListener

  // tag::factory-bean[]
  static class Config {
    @Bean
    FactoryBean<ApplicationListener<MentionEvent>> mentionListener() {
      new SpockMockFactoryBean(ApplicationListener)
    }
  }
  // end::factory-bean[]

  def "injected listener is a mock"() {
    expect:
    new MockUtil().isMock(mentionListener) // <4>
  }
}
