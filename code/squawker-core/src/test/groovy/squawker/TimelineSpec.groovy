/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package squawker

import java.time.Clock
import java.time.Instant
import spock.lang.Shared
import spock.lang.Subject
import squawker.jdbi.BasePersistenceSpec
import squawker.jdbi.FollowingStore
import squawker.jdbi.MessageStore
import static java.time.Instant.now
import static java.time.ZoneOffset.UTC
import static java.time.temporal.ChronoUnit.MINUTES

class TimelineSpec extends BasePersistenceSpec {

  @Shared MessageStore messageStore
  @Shared FollowingStore followingStore

  @Subject user = new User("khan")
  def followedUser = new User("kirk")
  def otherUser = new User("spock")

  def setupSpec() {
    messageStore = handle.attach(MessageStore)
    messageStore.createMessageTable()

    followingStore = handle.attach(FollowingStore)
    followingStore.createFollowingTable()
  }

  def cleanupSpec() {
    handle.execute("drop table message if exists")
    handle.execute("drop table following if exists")
  }

  def setup() {
    [user, followedUser, otherUser].each { userStore.insert(it.username) }
    followingStore.follow(user, followedUser)

    def now = now()
    postMessage otherUser, now.minus(6, MINUTES),
      "His pattern indicates two-dimensional thinking."
    postMessage user, now.minus(5, MINUTES),
      "@kirk You're still alive, my old friend?"
    postMessage followedUser, now.minus(4, MINUTES),
      "@khan KHAAANNNN!"
    postMessage followedUser, now.minus(3, MINUTES),
      "@scotty I need warp speed in three minutes or we're all dead!"
    postMessage otherUser, now.minus(2, MINUTES),
      "@bones I'm sorry, Doctor, I have no time to explain this logically."
    postMessage user, now.minus(1, MINUTES),
      "It is very cold in space!"
  }

  def cleanup() {
    handle.execute("delete from message")
    handle.execute("delete from following")
  }

  def "a user's timeline contains posts from themselves and followed users"() {
    expect:
    with(messageStore.timeline(user)) {
      size() == 4

      postedBy.every {
        it in [user, followedUser]
      }

      !postedBy.any {
        it == otherUser
      }
    }
  }

  def "a user's timeline is ordered most recent first"() {
    expect:
    with(messageStore.timeline(user)) {
      postedAt == postedAt.sort().reverse()
    }
  }

  private void postMessage(User poster, Instant at, String text) {
    def clock = Clock.fixed(at, UTC)
    messageStore.insert(poster, text, clock.instant())
  }
}
