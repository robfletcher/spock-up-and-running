package squawker.cardinality.v6

import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import squawker.Specification
import static org.hamcrest.Matchers.containsInAnyOrder
import static spock.util.matcher.HamcrestSupport.expect

class TimelineSpec extends Specification {

  DBI dbi = new DBI(dataSource)

  def cleanup() {
    dbi.withHandle { Handle handle ->
      handle.execute("delete from following") // <4>
      handle.execute("delete from message")
      handle.execute("delete from user")
    }
  }

  def "a user's timeline can contain multiple messages from each user"() {
    when:
    def timeline = messageStore.timeline(user)

    // tag::hamcrest[]
    then:
    expect timeline.postedBy, containsInAnyOrder(
      user, user, followedUser, followedUser
    )
    // end::hamcrest[]
  }
}
