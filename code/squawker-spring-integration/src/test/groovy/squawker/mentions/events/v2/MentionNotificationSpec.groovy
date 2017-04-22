package squawker.mentions.events.v2

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationListener
import org.springframework.test.annotation.DirtiesContext
import squawker.mentions.events.MentionEvent
import squawker.mentions.events.Specification
import squawker.spring.Main

// tag::bean-delegate[]
@SpringBootTest(classes = [
  Main,
  DelegatingApplicationListener // <1>
])
@DirtiesContext
class MentionNotificationSpec extends Specification {

  @Autowired DelegatingApplicationListener listener // <2>

  ApplicationListener<MentionEvent> mentionListener = Mock()

  def setup() {
    listener.delegate = mentionListener // <3>
  }

  static class DelegatingApplicationListener implements ApplicationListener<MentionEvent> {
    @Delegate ApplicationListener<MentionEvent> delegate // <4>
  }
// end::bean-delegate[]
}
