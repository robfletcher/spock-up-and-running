package squawker.cardinality.v4

import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import squawker.Specification

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

    // tag::count-by[]
    then:
    timeline.countBy { it.postedBy } == [(user): 2, (followedUser): 2]
    // end::count-by[]
  }
}
