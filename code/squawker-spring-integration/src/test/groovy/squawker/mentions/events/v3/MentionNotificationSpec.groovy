package squawker.mentions.events.v3

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import spock.mock.DetachedMockFactory
import squawker.mentions.events.MentionEvent
import squawker.mentions.events.Specification
import squawker.spring.Main

// tag::mock-beans[]
@SpringBootTest(classes = [
  Main,
  Config // <1>
])
class MentionNotificationSpec extends Specification {

  @Autowired ApplicationListener<MentionEvent> mentionListener // <2>

  static class Config {
    private final mockFactory = new DetachedMockFactory() // <3>

    @Bean
    ApplicationListener<MentionEvent> mentionListener() {
      mockFactory.Mock(ApplicationListener)
    }
  }
// end::mock-beans[]
}
