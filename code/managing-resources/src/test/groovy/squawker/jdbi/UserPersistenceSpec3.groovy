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
import co.freeside.jdbi.time.TimeTypesArgumentFactory
import co.freeside.jdbi.time.TimeTypesMapperFactory
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import org.skife.jdbi.v2.util.IntegerColumnMapper
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import squawker.User
import squawker.time.*
import static java.time.Instant.now
import static java.time.ZoneOffset.UTC

class UserPersistenceSpec3 extends Specification {

  // tag::shared-table[]
  @Subject @Shared DataStore dataStore // <1>

  @Shared dbi = new DBI("jdbc:h2:mem:test")
  @Shared Handle handle // <2>

  def setupSpec() {
    dbi.registerArgumentFactory(new TimeTypesArgumentFactory())
    dbi.registerMapper(new TimeTypesMapperFactory())

    handle = dbi.open() // <3>
    dataStore = handle.attach(DataStore)
    dataStore.createUserTable()
  }

  def cleanupSpec() { // <4>
    handle.execute("drop table user if exists")
    handle.close()
  }

  def cleanup() {
    handle.execute("delete from user") // <5>
  }
  // end::shared-table[]

  // tag::data-mixed-with-logic[]
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
  // end::data-mixed-with-logic[]

  // tag::expect-precondition[]
  def "can insert a user object"() {
    given:
    def clock = Clock.fixed(now(), UTC)

    and:
    def user = new User("spock", clock.instant())

    expect:
    rowCount("user") == 0 // <1>

    when:
    dataStore.insert(user)

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

  private int rowCount(String table) {
    handle.createQuery("select count(*) from $table")
          .map(IntegerColumnMapper.PRIMITIVE)
          .first()
  } // <2>
  // end::expect-precondition[]

}
