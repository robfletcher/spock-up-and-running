package squawker.cleanup.v6

import co.freeside.jdbi.time.TimeTypesArgumentFactory
import co.freeside.jdbi.time.TimeTypesMapperFactory
import org.h2.jdbcx.JdbcDataSource
import org.skife.jdbi.v2.DBI
import spock.lang.*
import squawker.User
import squawker.cleanup.v5.TruncateTables
import squawker.jdbi.FollowingStore
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore
import static java.time.Instant.now
import static java.time.temporal.ChronoUnit.MINUTES
import static org.hamcrest.Matchers.containsInAnyOrder
import static spock.util.matcher.HamcrestSupport.expect

// tag::narrative[]
@Title("Timeline")
@Narrative("""
A user can access a 'timeline' -- a reverse-chronologically ordered list
of messages posted by themselves and any users they follow.
Messages posted by users they do not follow should not appear in the timeline.
""")
class TimelineSpec extends Specification {

  // ...
// end::narrative[]

  @Shared
  def dataSource = new JdbcDataSource(
    url: "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false"
  )

  @TruncateTables
  DBI dbi = new DBI(dataSource)

  UserStore userStore
  MessageStore messageStore
  FollowingStore followingStore

  @Subject user = new User("khan")
  def followedUser = new User("kirk")
  def otherUser = new User("spock")

  def setup() {
    dbi.registerArgumentFactory(new TimeTypesArgumentFactory())
    dbi.registerMapper(new TimeTypesMapperFactory())

    userStore = dbi.onDemand(UserStore)
    userStore.createUserTable()

    messageStore = dbi.onDemand(MessageStore)
    messageStore.createMessageTable()

    followingStore = dbi.onDemand(FollowingStore)
    followingStore.createFollowingTable()

    [user, followedUser, otherUser].each {
      userStore.insert(it.username)
    }
    followingStore.follow(user, followedUser)

    def now = now()
    messageStore.insert(
      otherUser,
      "His pattern indicates two-dimensional thinking.",
      now.minus(6, MINUTES))
    messageStore.insert(
      user,
      "@kirk You're still alive, my old friend?",
      now.minus(5, MINUTES))
    messageStore.insert(
      followedUser,
      "@khan KHAAANNNN!",
      now.minus(4, MINUTES))
    messageStore.insert(
      followedUser,
      "@scotty I need warp speed in three minutes or we're all dead!",
      now.minus(3, MINUTES))
    messageStore.insert(
      otherUser, "@bones I'm sorry, Doctor, I have no time to explain this logically.",
      now.minus(2, MINUTES))
    messageStore.insert(
      user,
      "It is very cold in space!",
      now.minus(1, MINUTES))
  }
// tag::narrative[]

  def "a user's timeline does not contains posts by users they do not follow"() {
    when: "a user retrieves their timeline"
    def timeline = messageStore.timeline(user)

    then: "it contains some messages"
    !timeline.empty

    and: "it does not contain any messages posted by users they do not follow"
    !timeline.postedBy.any {
      it == otherUser
    }
  }

  def "a user's timeline is ordered most recent first"() {
    when: "a user retrieves their timeline"
    def timeline = messageStore.timeline(user)

    then: "it contains some messages"
    !timeline.empty

    and: "the messages are ordered most recent first"
    timeline.postedAt == timeline.postedAt.sort().reverse()
  }

  def "a user's timeline can contain multiple messages from each user"() {
    when: "a user retrieves their timeline"
    def timeline = messageStore.timeline(user)

    then: "it may contain multiple messages from the same user"
    expect timeline.postedBy, containsInAnyOrder(
      user, user, followedUser, followedUser
    )
  }
}
// end::narrative[]
