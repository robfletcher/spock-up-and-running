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
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import squawker.User
import squawker.jdbi.FollowingStore
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore

abstract class TimelineSpec7 extends Specification {

  @Shared dbi = new DBI("jdbc:h2:mem:test")

  def setupSpec() {
    dbi.registerArgumentFactory(new TimeTypesArgumentFactory())
    dbi.registerMapper(new TimeTypesMapperFactory())
  }

  // tag::autocleanup[]
  @AutoCleanup Handle handle

  def cleanup() {
    handle.execute("drop table user if exists")
    handle.execute("drop table message if exists")
    handle.execute("drop table following if exists")
  }
  // end::autocleanup[]

  UserStore userStore
  MessageStore messageStore
  FollowingStore followingStore

  @Subject User user
  @Delegate FixturesDelegate fixtures

  def setup() {
    handle = dbi.open()

    userStore = handle.attach(UserStore)
    userStore.createUserTable()

    messageStore = handle.attach(MessageStore)
    messageStore.createMessageTable()

    followingStore = handle.attach(FollowingStore)
    followingStore.createFollowingTable()

    user = userStore.insert("khan")

    fixtures = new FixturesDelegate(
      messageStore,
      userStore,
      followingStore,
      user
    )
  }

  static class ReturnsBoolean extends TimelineSpec7 {

    def "a user's timeline contains posts from themselves and followed users"() {
      given:
      def followedUser = followNewUser("kirk")
      def otherUser = newUser("spock")
      [user, followedUser, otherUser].each { poster ->
        2.times { postMessageBy(poster) }
      }

      expect:
      timelineSizeIs(4)
    }

    // tag::helper-returns-boolean[]
    boolean timelineSizeIs(int expectedSize) {
      messageStore.timeline(user).size() == expectedSize
    }
    // end::helper-returns-boolean[]
  }

  static class WithAssertKeyword extends TimelineSpec7 {

    def "a user's timeline contains posts from themselves and followed users"() {
      given:
      def followedUser = followNewUser("kirk")
      def otherUser = newUser("spock")
      [user, followedUser, otherUser].each { poster ->
        2.times { postMessageBy(poster) }
      }

      expect:
      timelineSizeIs(4)
    }

    // tag::helper-with-assertion[]
    void timelineSizeIs(int expectedSize) {
      assert messageStore.timeline(user).size() == expectedSize
    }
    // end::helper-with-assertion[]
  }
}
