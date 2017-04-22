package squawker.jdbi.async

import com.oreilly.spock.FailsWithCondition
import spock.util.concurrent.BlockingVariables
import static java.time.Instant.now

class AsyncMessageStoreSpec5 extends BaseAsyncMessageStoreSpec {

  @FailsWithCondition("""\
followedUsernames .collectEntries { [(it): messages[it].text] } // <1> .every { it.value == "Hi @\$username from \$it.key" } // <2>
|                  |
|                  [kirk:Hi @spock from @kirk, bones:Hi @spock from @bones, sulu:Hi @spock from @sulu]
[kirk, bones, sulu]
""")
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
    def messages = new BlockingVariables()
    asyncMessageStore.latestPostsByFollowed(user) { message ->
      messages[message.postedBy.username] = message
    }

    // tag::improve-diagnostics[]
    then:
    followedUsernames
      .collectEntries { [(it): messages[it].text] } // <1>
      .every { it.value == "Hi @$username from $it.key" } // <2>
    // end::improve-diagnostics[]

    where:
    username = "spock"
    followedUsernames = ["kirk", "bones", "sulu"]
  }

  def "correct assertion"() {
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
    followedUsernames
      .collectEntries { [(it): messages[it].text] }
      .every { it.value == "Hi @$username from @$it.key" }

    where:
    username = "spock"
    followedUsernames = ["kirk", "bones", "sulu"]
  }
}
