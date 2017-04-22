package squawker.mentions.events

import com.oreilly.spock.TruncateTables
import org.skife.jdbi.v2.DBI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import spock.util.concurrent.BlockingVariable
import squawker.jdbi.UserStore
import squawker.mentions.DBIConnector
import squawker.mentions.MessageService

abstract class Specification extends spock.lang.Specification {

  abstract ApplicationListener<MentionEvent> getMentionListener()
  // tag::base-spec[]

  @Autowired @TruncateTables(DBIConnector) DBI dbi
  @Autowired UserStore userStore
  @Autowired MessageService messageService

  def "registered listeners are notified of mentions"() {
    given:
    def user = userStore.insert(postingUsername)
    userStore.insert(mentionedUsername)

    and:
    def event = new BlockingVariable<MentionEvent>()
    mentionListener.onApplicationEvent(_) >> { MentionEvent it ->
      event.set(it)
    }

    when:
    def message = messageService.postMessage(user, messageText)

    then:
    with(event.get()) {
      mentionedUsername == mentionedUsername
      message.id == message.id
    }

    where:
    postingUsername = "kirk"
    mentionedUsername = "spock"
    messageText = "Report, Mr @$mentionedUsername!"
  }
}
// end::base-spec[]
