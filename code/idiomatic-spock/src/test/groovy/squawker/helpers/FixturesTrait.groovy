package squawker.helpers

import java.time.Instant
import java.time.temporal.ChronoUnit
import groovy.transform.CompileStatic
import squawker.Message
import squawker.User
import squawker.jdbi.FollowingStore
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore

@CompileStatic
//tag::fixture-trait[]
trait FixturesTrait {

  abstract MessageStore getMessageStore() // <1>
  abstract UserStore getUserStore()

  abstract FollowingStore getFollowingStore()

  abstract User getUser()

  void postMessageBy(User poster) {
    messageStore.insert(poster, "aaaa", Instant.now())
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

  User newUser(String username) {
    userStore.insert(username)
  }

  List<Message> getTimeline() {
    messageStore.timeline(user)
  }
}
