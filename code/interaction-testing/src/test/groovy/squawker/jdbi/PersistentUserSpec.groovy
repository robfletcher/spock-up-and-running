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

import org.skife.jdbi.v2.exceptions.UnableToCreateStatementException
import spock.lang.Specification
import spock.lang.Subject
import squawker.User
import static java.time.Instant.now

class PersistentUserSpec extends Specification {

  // tag::mock-verification[]
  def dataStore = Mock(DataStore) // <1>
  @Subject user = new PersistentUser(dataStore, "spock", now()) // <2>

  def "following another user is persisted"() {
    given:
    def other = new User("kirk")

    when:
    user.follow(other) // <3>

    then:
    1 * dataStore.follow(user, other) // <4>
  }
  // end::mock-verification[]

  // tag::mock-cardinality[]
  def "the following list is read from the database and cached"() {
    given:
    def otherUsers = ["kirk", "bones", "scotty"].collect {
      new User(it, now())
    }

    when: "the list of followed users is requested multiple times"
    def result1 = user.following
    def result2 = user.following

    then: "the database is queried only once"
    1 * dataStore.findFollowing(user) >> otherUsers

    and: "both calls return consistent results"
    result1 == otherUsers as Set
    result2 == result1
  }
  // end::mock-cardinality[]

  // tag::throw-from-mock[]
  def "an exception is thrown if the database connection is stale"() {
    when:
    user.posts

    then:
    1 * dataStore.postsBy(user) >> {
      throw new UnableToCreateStatementException(null)
    } // <1>

    and:
    def e = thrown(IllegalStateException) // <2>
    e.cause instanceof UnableToCreateStatementException
  }
  // end::throw-from-mock[]
}
