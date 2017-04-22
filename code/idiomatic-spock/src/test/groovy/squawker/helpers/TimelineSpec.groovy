/*
 * Copyright 2015 the original author or authors.
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
package squawker.helpers

import co.freeside.jdbi.time.TimeTypesArgumentFactory
import co.freeside.jdbi.time.TimeTypesMapperFactory
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import squawker.User
import squawker.jdbi.FollowingStore
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore
import static java.time.Instant.now
import static java.time.temporal.ChronoUnit.MINUTES

class TimelineSpec extends Specification {

  @Shared dbi = new DBI("jdbc:h2:mem:test")
  Handle handle

  def setupSpec() {
    dbi.registerArgumentFactory(new TimeTypesArgumentFactory())
    dbi.registerMapper(new TimeTypesMapperFactory())
  }

  def cleanup() {
    handle.execute("drop table user if exists")
    handle.execute("drop table message if exists")
    handle.execute("drop table following if exists")
    handle.close()
  }

  UserStore userStore
  MessageStore messageStore
  FollowingStore followingStore

  // tag::inline-setup[]
  @Subject user = new User("khan")
  def followedUser = new User("kirk")
  def otherUser = new User("spock")

  def setup() {
    handle = dbi.open()

    userStore = handle.attach(UserStore)
    userStore.createUserTable()

    messageStore = handle.attach(MessageStore)
    messageStore.createMessageTable()

    followingStore = handle.attach(FollowingStore)
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

  def "a user's timeline contains posts from themselves and followed users"() {
    expect:
    with(messageStore.timeline(user)) {
      size() == 4
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
  // end::inline-setup[]
}
