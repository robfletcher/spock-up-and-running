package squawker.stepwise

import co.freeside.jdbi.time.TimeTypesArgumentFactory
import co.freeside.jdbi.time.TimeTypesMapperFactory
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import squawker.User
import squawker.helpers.FixturesDelegate
import squawker.jdbi.FollowingStore
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore

// tag::stepwise-spec[]
@Stepwise
class TimelineSpec extends Specification {

  @Shared dbi = new DBI("jdbc:h2:mem:test")

  // <1>
  @Shared Handle handle
  @Shared UserStore userStore
  @Shared MessageStore messageStore
  @Shared FollowingStore followingStore
  @Shared User user

  def setupSpec() {
    dbi.registerArgumentFactory(new TimeTypesArgumentFactory())
    dbi.registerMapper(new TimeTypesMapperFactory())

    handle = dbi.open()

    userStore = handle.attach(UserStore)
    userStore.createUserTable()

    messageStore = handle.attach(MessageStore)
    messageStore.createMessageTable()

    followingStore = handle.attach(FollowingStore)
    followingStore.createFollowingTable()

    user = userStore.insert("khan")
  }

  def cleanupSpec() { // <2>
    dbi.withHandle { handle ->
      handle.execute("drop table user if exists")
      handle.execute("drop table message if exists")
      handle.execute("drop table following if exists")
    }
  }

  @Delegate FixturesDelegate fixtures // <3>

  def setup() {
    fixtures = new FixturesDelegate(
      messageStore,
      userStore,
      followingStore,
      user
    )
  }

  def followedUsername = "kirk"
  def otherUsername = "spock"

  def "a user's timeline contains posts from themselves and followed users"() {
    given:
    def followedUser = followNewUser(followedUsername)
    def otherUser = newUser(otherUsername)
    [user, followedUser, otherUser].each { poster ->
      2.times { postMessageBy(poster) }
    }

    expect:
    timeline.size() == 4
    !timeline.postedBy.username.contains(otherUsername)
  }

  def "when new messages are posted they appear in the timeline"() {
    when:
    postMessageBy(followedUsername) // <4>

    then:
    timeline.size() == 5
    timeline.first().postedAt > old(timeline.first().postedAt)
  }

  def "after following a user their posts appear in the timeline"() {
    expect:
    !messageStore.postsBy(otherUsername).empty

    when:
    followExistingUser(otherUsername)

    then:
    timeline.size() > old(timeline.size())
    timeline.postedBy.username.contains(otherUsername)
  }
}
// end::stepwise-spec[]
