package squawker.grammar

import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException

class TimelineSpec3 extends Specification {

  // tag::where[]
  def "a user who does not follow anyone sees only their own messages"() {
    given:
    def user = newUser(username)
    3.times { postMessageBy(user) }

    and:
    def other = newUser(otherUsername)
    3.times { postMessageBy(other) }

    expect:
    user.timeline().postedBy.every {
      it == user
    }

    where:
    username = "spock"
    otherUsername = "kirk"
  }
  // end::where[]

  // tag::separate-precondition[]
  def "a user cannot follow someone they already follow"() {
    given:
    def user = userStore.insert(username)
    def other = userStore.insert(otherUsername)

    and:
    followingStore.follow(user, other)

    when:
    followingStore.follow(user, other)

    then:
    thrown UnableToExecuteStatementException

    where:
    username = "spock"
    otherUsername = "kirk"
  }
  // end::separate-precondition[]

}
