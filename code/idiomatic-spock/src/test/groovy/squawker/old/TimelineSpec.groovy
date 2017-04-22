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

package squawker.old

import co.freeside.jdbi.time.TimeTypesArgumentFactory
import co.freeside.jdbi.time.TimeTypesMapperFactory
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import squawker.helpers.FixturesDelegate
import squawker.User
import squawker.jdbi.FollowingStore
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore

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

  // tag::hardcoded-values[]
  def "when new messages are posted they appear in the timeline"() {
    given:
    def followedUser = followNewUser("kirk")

    and:
    postMessageBy(followedUser)

    expect:
    timeline.size() == 1

    when:
    postMessageBy(followedUser)

    then:
    // tag::assertion-helper[]
    timeline.size() == 2
    // end::assertion-helper[]
  }
  // end::hardcoded-values[]
}

