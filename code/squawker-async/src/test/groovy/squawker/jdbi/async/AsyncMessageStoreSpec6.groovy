package squawker.jdbi.async

import spock.util.concurrent.PollingConditions
import static java.time.Instant.now

class AsyncMessageStoreSpec6 extends BaseAsyncMessageStoreSpec {

  def "can retrieve latest message by all followers"() {
    given:
    def user = userStore.insert(username)
    followedUsernames.each {
      def followed = userStore.insert(it)
      user.follow(followed)
      followed.post("Older message", now().minusSeconds(5))
      followed.post("Hi @$username from @$it", now())
    }

    expect:
    user.following*.username.containsAll(followedUsernames)

    // tag::polling-conditions[]
    when:
    def messages = [] // <1>
    asyncMessageStore.latestPostsByFollowed(user) { message ->
      messages << message
    }

    then:
    def conditions = new PollingConditions() // <2>
    conditions.eventually {
      assert messages.text.containsAll(expectedMessages) // <3>
    }

    where:
    username = "spock"
    followedUsernames = ["kirk", "bones", "sulu"]
    expectedMessages = followedUsernames.collect {
      "Hi @$username from @$it".toString()
    }
    // end::polling-conditions[]
  }
}
