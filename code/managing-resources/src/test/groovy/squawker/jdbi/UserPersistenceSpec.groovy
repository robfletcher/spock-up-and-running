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

import java.time.*
import co.freeside.jdbi.time.*
import org.skife.jdbi.v2.*
import spock.lang.*
import squawker.*
import squawker.time.*
import static java.time.Instant.*
import static java.time.ZoneOffset.*

class UserPersistenceSpec extends Specification {

  // tag::managed-resources[]
  @Subject DataStore dataStore

  def dbi = new DBI("jdbc:h2:mem:test")
  Handle handle

  def setup() {
    dbi.registerArgumentFactory(new TimeTypesArgumentFactory())
    dbi.registerMapper(new TimeTypesMapperFactory())

    handle = dbi.open()
    dataStore = handle.attach(DataStore)

    dataStore.createUserTable()
  }

  def cleanup() {
    handle.execute("drop table user if exists")
    handle.close()
  }
  // end::managed-resources[]

  // tag::read-persisted-data[]
  def "can retrieve a list of user objects"() {
    given:
    def timestamp = LocalDateTime.of(1966, 9, 8, 20, 0).toInstant(UTC)
    ["kirk", "spock"].each {
      handle.createStatement("""insert into user (username, registered)
                                values (?, ?)""")
            .bind(0, it)
            .bind(1, timestamp)
            .execute()
    }

    when:
    def users = dataStore.findAllUsers()

    then:
    with(users.toList()) {
      username == ["kirk", "spock"]
      registered.every {
        it == timestamp
      }
    }
  }
  // end::read-persisted-data[]

  // tag::first-persistence-test[]
  def "can insert a user object"() {
    given:
    def clock = Clock.fixed(now(), UTC) // <1>

    and:
    def user = new User("spock", clock.instant()) // <2>

    when:
    dataStore.insert(user) // <3>

    then:
    def iterator = handle.createQuery("select username, registered from user")
                         .iterator() // <4>
    iterator.hasNext() // <5>
    with(iterator.next()) {
      username == user.username
      registered.time == clock.instant().toEpochMilli()
    } // <6>

    and:
    !iterator.hasNext() // <7>
  }
  // end::first-persistence-test[]
}
