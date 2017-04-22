package squawker.jdbi.async

import java.util.concurrent.atomic.AtomicReference
import com.oreilly.spock.FailsWithCondition
import spock.util.concurrent.BlockingVariable
import squawker.Message
import static java.time.Instant.now

class AsyncMessageStoreSpec extends BaseAsyncMessageStoreSpec {

  // tag::naive-impl[]
  def "retrieves the latest post by a user"() {
    given:
    userStore.insert(username).with { user ->
      user.post(messageText, now())
    }

    expect:
    asyncMessageStore.latestPostBy(username) {
      assert it.text == messageText // <1>
    }

    where:
    username = "spock"
    messageText = "Fascinating!"
  }
  // end::naive-impl[]

  @FailsWithCondition("""\
callback.get().text == messageText
|        |     |
|        null  java.lang.NullPointerException: Cannot get property 'text' on null object
squawker.Message@2
""")
  def "asserting too soon"() {
    given:
    def user = userStore.insert("spock")

    and:
    user.post(messageText, now())

    when:
    def callback = new AtomicReference<Message>()
    asyncMessageStore.latestPostBy(user.username, callback.&set)

    then:
    callback.get().text == messageText

    where:
    messageText = "Fascinating!"
  }

  def "blocking callback"() {
    given:
    def user = userStore.insert("spock")

    and:
    user.post(messageText, now())

    when:
    def callback = new BlockingVariable<Message>()
    asyncMessageStore.latestPostBy(user.username, callback.&set)

    then:
    callback.get().text == messageText

    where:
    messageText = "Fascinating!"
  }

}
