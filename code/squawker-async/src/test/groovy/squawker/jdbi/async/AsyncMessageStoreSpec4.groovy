package squawker.jdbi.async

import com.oreilly.spock.FailsWithCondition
import spock.util.concurrent.BlockingVariable
import spock.util.concurrent.BlockingVariables
import squawker.Message
import static java.time.Instant.now

class AsyncMessageStoreSpec4 extends BaseAsyncMessageStoreSpec {

  def "retrieves the latest post by a user"() {
    given:
    userStore.insert(username).with { user ->
      user.post(messageText, now())
    }

    // tag::with-blocking-variable[]
    expect:
    with(new BlockingVariable<Message>()) { callback ->
      asyncMessageStore.latestPostBy(username, callback.&set)
      callback.get().text == messageText
    }
    // end::with-blocking-variable[]

    where:
    username = "spock"
    messageText = "Fascinating!"
  }

  // tag::capture-multiple-callbacks[]
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
    def messages = new BlockingVariables() // <1>
    asyncMessageStore.latestPostsByFollowed(user) { message ->
      messages[message.postedBy.username] = message // <2>
    }

    then:
    followedUsernames.every {
      messages[it].text == "Hi @$username from @$it" // <3>
    }

    where:
    username = "spock"
    followedUsernames = ["kirk", "bones", "sulu"]
  }
  // end::capture-multiple-callbacks[]

  @FailsWithCondition("""\
followedUsernames.every { messages[it].text == "Hi @\$username from \$it" }
|                 |
|                 false
[kirk, bones, sulu]
""")
  def "assertion is incorrect"() {
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
    def messages = new BlockingVariables()
    asyncMessageStore.latestPostsByFollowed(user) { message ->
      messages[message.postedBy.username] = message
    }

    then:
    // tag::incorrect-assertion[]
    followedUsernames.every {
      messages[it].text == "Hi @$username from $it"
    }
    // end::incorrect-assertion[]

    where:
    username = "spock"
    followedUsernames = ["kirk", "bones", "sulu"]
  }

}
