package squawker.grammar

import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException

class TimelineSpec extends Specification {

  // tag::inappropriate-when-then[]
  def "a user who does not follow anyone sees only their own messages"() {
    when:
    def user = newUser("spock")
    3.times { postMessageBy(user) }
    def other = newUser("kirk")
    3.times { postMessageBy(other) }

    then:
    user.timeline().postedBy.every {
      it == user
    }
  }
  // end::inappropriate-when-then[]

  // tag::run-on-when[]
  def "a user cannot follow someone they already follow"() {
    when:
    def user = userStore.insert("spock")
    def other = userStore.insert("kirk")
    followingStore.follow(user, other)
    followingStore.follow(user, other)

    then:
    thrown UnableToExecuteStatementException
  }
  // end::run-on-when[]

}
