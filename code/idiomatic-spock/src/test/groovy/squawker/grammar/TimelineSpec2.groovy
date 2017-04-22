package squawker.grammar

import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException

class TimelineSpec2 extends Specification {

  // tag::given-expect[]
  def "a user who does not follow anyone sees only their own messages"() {
    given:
    def user = newUser("spock")
    3.times { postMessageBy(user) }
    def other = newUser("kirk")
    3.times { postMessageBy(other) }

    expect:
    user.timeline().postedBy.every {
      it == user
    }
  }
  // end::given-expect[]

  // tag::separate-precondition[]
  def "a user cannot follow someone they already follow"() {
    given:
    def user = userStore.insert("spock")
    def other = userStore.insert("kirk")
    followingStore.follow(user, other)

    when:
    followingStore.follow(user, other)

    then:
    thrown UnableToExecuteStatementException
  }
  // end::separate-precondition[]

}
