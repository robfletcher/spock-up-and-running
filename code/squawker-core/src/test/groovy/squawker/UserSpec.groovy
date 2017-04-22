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

import java.time.Instant
import spock.lang.Specification
import spock.lang.Unroll

class UserSpec extends Specification {

  @Unroll("cannot construct a user with #description for a username")
  def "cannot construct a user without a username"() {
    when:
    new User(username)

    then:
    thrown IllegalArgumentException

    where:
    username << [null, ""]
    description << ["null", "the empty String"]
  }

  def "initial state of a user is correct"() {
    given:
    def user = new User("kirk")

    expect:
    with(user) {
      username == "kirk"
      following.isEmpty()
      posts.isEmpty()
      registered instanceof Instant
    }
  }

  @Unroll("a user with username '#username1' #description another with username '#username2'")
  def "equality is based on the username"() {
    expect:
    user1 == user2 ^ !isEqual

    where:
    username1 | username2
    "kirk"    | "kirk"
    "kirk"    | "spock"

    and:
    user1 = new User(username1)
    user2 = new User(username2)

    and:
    isEqual = username1 == username2
    description = isEqual ? "is equal to" : "is not equal to"
  }

  def "hashCode is equal if users are equal"() {
    given:
    def user1 = new User("kirk")
    def user2 = new User("kirk")

    expect:
    user1.hashCode() == user2.hashCode()
  }

}
