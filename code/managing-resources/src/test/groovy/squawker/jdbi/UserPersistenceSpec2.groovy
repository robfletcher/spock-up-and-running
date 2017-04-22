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

class UserPersistenceSpec2 extends Specification {

  // tag::shared-resources[]
  @Subject DataStore dataStore

  @Shared dbi = new DBI("jdbc:h2:mem:test") // <1>
  Handle handle

  def setupSpec() { // <2>
    dbi.registerArgumentFactory(new TimeTypesArgumentFactory())
    dbi.registerMapper(new TimeTypesMapperFactory())
  }

  def setup() {
    handle = dbi.open()
    dataStore = handle.attach(DataStore)

    dataStore.createUserTable()
  }

  def cleanup() {
    handle.execute("drop table user if exists")
    handle.close()
  }
  // end::shared-resources[]

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

  def "can insert a user object"() {
    given:
    def clock= Clock.fixed(now(), UTC)

    and:
    def user = new User("spock", clock.instant())

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

}
