package squawker.jdbi.async

import spock.util.concurrent.PollingConditions
import static java.time.Instant.now

class AsyncMessageStoreSpec7 extends BaseAsyncMessageStoreSpec {

  // tag::polling-conditions-delegate[]
  @Delegate PollingConditions conditions = new PollingConditions()
  // end::polling-conditions-delegate[]

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

    when:
    def messages = []
    asyncMessageStore.latestPostsByFollowed(user) { message ->
      messages << message
    }

    // tag::polling-conditions-use[]
    then:
    eventually {
      assert messages.text.containsAll(expectedMessages)
    }
    // end::polling-conditions-use[]

    where:
    username = "spock"
    followedUsernames = ["kirk", "bones", "sulu"]
    expectedMessages = followedUsernames.collect {
      "Hi @$username from @$it".toString()
    }
  }
}
