package squawker.jdbi.async

import spock.util.concurrent.BlockingVariable
import squawker.Message
import static java.time.Instant.now

class AsyncMessageStoreSpec2 extends BaseAsyncMessageStoreSpec {

  // tag::blocking-variable[]
  def "retrieves the latest post by a user"() {
    given:
    userStore.insert(username).with { user ->
      user.post(messageText, now())
    }

    when:
    def result = new BlockingVariable<Message>() // <1>
    asyncMessageStore.latestPostBy(username) { message ->
      result.set(message) // <2>
    }

    then:
    result.get().text == messageText // <3>

    where:
    username = "spock"
    messageText = "Fascinating!"
  }
  // end::blocking-variable[]

}
