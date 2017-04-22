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
package squawker

import co.freeside.jdbi.time.TimeTypesArgumentFactory
import co.freeside.jdbi.time.TimeTypesMapperFactory
import org.h2.jdbcx.JdbcDataSource
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Subject
import squawker.jdbi.FollowingStore
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore
import static java.time.Instant.now
import static java.time.temporal.ChronoUnit.MINUTES

abstract class Specification extends spock.lang.Specification {

  // tag::base-cleanup-1[]
  @Shared
  def dataSource = new JdbcDataSource(
    url: "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false"
  )

  // end::base-cleanup-1[]
  abstract DBI getDbi()
  // tag::base-cleanup-2[]
  @AutoCleanup Handle handle
  // end::base-cleanup-2[]
  // tag::base-cleanup-3[]

  UserStore userStore
  MessageStore messageStore
  FollowingStore followingStore

  @Subject User user
  User followedUser
  User otherUser

  def setup() {
    dbi.registerArgumentFactory(new TimeTypesArgumentFactory())
    dbi.registerMapper(new TimeTypesMapperFactory())

    // tag::fixtures[]
    handle = dbi.open()

    userStore = handle.attach(UserStore)
    userStore.createUserTable()

    messageStore = handle.attach(MessageStore)
    messageStore.createMessageTable()

    followingStore = handle.attach(FollowingStore)
    followingStore.createFollowingTable()

    user = userStore.insert("khan")
    followedUser = userStore.insert("kirk")
    otherUser = userStore.insert("spock")
    user.follow(followedUser)

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
    // end::fixtures[]
  }

  // end::base-cleanup-3[]
  // tag::base-cleanup-4[]

  def "a user's timeline does not contains posts by users they do not follow"() {
    when:
    def timeline = messageStore.timeline(user)

    then:
    !timeline.empty

    and:
    !timeline.postedBy.any {
      it == otherUser
    }
  }

  def "a user's timeline is ordered most recent first"() {
    when:
    def timeline = messageStore.timeline(user)

    then:
    !timeline.empty

    and:
    timeline.postedAt == timeline.postedAt.sort().reverse()
  }
  // end::base-cleanup-4[]
}
