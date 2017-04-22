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
package squawker.jdbi

import java.time.Clock
import java.time.LocalDateTime
import spock.lang.Unroll
import squawker.User
import squawker.time.*
import static java.time.Instant.now
import static java.time.ZoneOffset.UTC

class UserStoreSpec extends BasePersistenceSpec {

  // tag::where-without-iterating-2[]
  def "can retrieve a list of user objects"() {
    given:
    usernames.each { // <2>
      handle.createStatement("""insert into user (username, registered)
                                values (?, ?)""")
            .bind(0, it)
            .bind(1, timestamp) // <3>
            .execute()
    }

    when:
    def users = userStore.findAllUsers()

    then:
    with(users.toList()) {
      username == usernames // <4>
      registered.every {
        it == timestamp
      }
    }

    where:
    usernames = ["kirk", "spock"] // <1>
    timestamp = LocalDateTime.of(1966, 9, 8, 20, 0).toInstant(UTC)
  }
  // end::where-without-iterating-2[]

  def "can insert a user object"() {
    given:
    def clock = Clock.fixed(now(), UTC)

    and:
    def user = new User("spock", clock.instant())

    expect:
    rowCount("user") == 0

    when:
    userStore.insert(user.username, clock)

    then:
    def iterator = handle.createQuery("select username, registered from user")
                         .iterator()
    iterator.hasNext()
    with(iterator.next()) {
      username == user.username
      registered.time == clock.instant().toEpochMilli()
    }

    and:
    !iterator.hasNext()
  }

  @Unroll("username '#username' is #description")
  def "can determine if a username is in use"() {
    given:
    userStore.insert("spock")

    expect:
    userStore.usernameInUse(username) == shouldExist

    where:
    username | shouldExist
    "spock"  | true
    "SPOCK"  | true
    "kirk"   | false

    description = shouldExist ? "in use" : "not in use"
  }
}
