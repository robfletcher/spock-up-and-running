package squawker.mentions.events.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import squawker.jdbi.MessageStore
import squawker.mentions.MentionStore
import squawker.mentions.events.MentionEvent
import squawker.mentions.events.Specification
import squawker.spring.Main

// tag::programatic-mock-beans[]
class MentionNotificationSpec extends Specification {

  def applicationContext = new AnnotationConfigApplicationContext() // <1>

  ApplicationListener<MentionEvent> mentionListener = Mock() // <2>

  @Autowired MessageStore messageStore
  @Autowired MentionStore mentionStore

  def setup() {
    applicationContext.with {
      register(Main) // <3>
      beanFactory.registerSingleton("mentionListener", mentionListener) // <4>
      refresh() // <5>
      beanFactory.autowireBean(this) // <6>
    }

    userStore.createUserTable() // <7>
    messageStore.createMessageTable()
    mentionStore.createMentionTable()
  }
// end::programatic-mock-beans[]
}
