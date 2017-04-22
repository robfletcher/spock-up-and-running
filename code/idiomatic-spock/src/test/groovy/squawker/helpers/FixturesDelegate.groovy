package squawker.helpers

import java.time.Instant
import java.time.temporal.ChronoUnit
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import squawker.Message
import squawker.User
import squawker.jdbi.FollowingStore
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore

@CompileStatic
//tag::fixture-delegate-class[]
@TupleConstructor
// <1>
class FixturesDelegate {

  final MessageStore messageStore // <2>
  final UserStore userStore
  final FollowingStore followingStore
  final User user

  void postMessageBy(User poster) {
    messageStore.insert(poster, "aaaa", Instant.now())
  }

  void postMessageBy(String posterName) {
    postMessageBy(userStore.find(posterName))
  }

  void postMessageAt(int minutesAgo) {
    def timestamp = Instant.now().minus(minutesAgo, ChronoUnit.MINUTES)
    messageStore.insert(user, "aaaaa", timestamp)
  }

  User followNewUser(String username) {
    def newUser = newUser(username)
    followingStore.follow(user, newUser)
    return newUser
  }

  void followExistingUser(String username) {
    def userToFollow = userStore.find(username)
    if (!userToFollow) throw new IllegalStateException("No such user $username")
    followingStore.follow(user, userToFollow)
  }

  User newUser(String username) {
    userStore.insert(username)
  }

  // tag::get-timeline-fixture[]
  List<Message> getTimeline() {
    messageStore.timeline(user)
  }
  // end::get-timeline-fixture[]
}
// end::fixture-delegate-class[]
