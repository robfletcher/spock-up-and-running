package squawker.jdbi.async

import spock.util.concurrent.BlockingVariable
import squawker.Message
import static java.time.Instant.now

class AsyncMessageStoreSpec3 extends BaseAsyncMessageStoreSpec {

  def "retrieves the latest post by a user"() {
    given:
    userStore.insert(username).with { user ->
      user.post(messageText, now())
    }

    // tag::blocking-variable-closure[]
    when:
    def callback = new BlockingVariable<Message>()
    asyncMessageStore.latestPostBy(username, callback.&set)

    then:
    callback.get().text == messageText
    // end::blocking-variable-closure[]

    where:
    username = "spock"
    messageText = "Fascinating!"
  }

}
