package squawker.helpers

import java.time.Instant
import java.time.temporal.ChronoUnit
import groovy.transform.CompileStatic
import squawker.User
import squawker.jdbi.FollowingStore
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore

@CompileStatic
// tag::static-import[]
class Fixtures {
  static void postMessageBy(MessageStore messageStore, User poster) {
    messageStore.insert(poster, "aaaa", Instant.now())
  }

  static void postMessageAt(MessageStore messageStore, User poster, int minutesAgo) {
    def timestamp = Instant.now().minus(minutesAgo, ChronoUnit.MINUTES)
    messageStore.insert(poster, "aaaaa", timestamp)
  }

  static User followNewUser(UserStore userStore, FollowingStore followingStore, User user, String username) {
    def newUser = newUser(userStore, username)
    followingStore.follow(user, newUser)
    return newUser
  }

  static User newUser(UserStore userStore, String username) {
    userStore.insert(username)
  }
}
