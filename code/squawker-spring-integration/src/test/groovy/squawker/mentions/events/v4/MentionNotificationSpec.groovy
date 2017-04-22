package squawker.mentions.events.v4

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationListener
import org.springframework.test.context.ContextConfiguration
import squawker.mentions.events.MentionEvent
import squawker.mentions.events.Specification
import squawker.spring.Main

// tag::xml[]
@ContextConfiguration(locations = "mention-notification-spec.xml")
// <1>
@SpringBootTest(classes = Main)
class MentionNotificationSpec extends Specification {

  @Autowired ApplicationListener<MentionEvent> mentionListener
// end::xml[]
}
