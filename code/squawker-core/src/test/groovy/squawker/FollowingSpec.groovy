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

import spock.lang.Specification
import spock.lang.Subject

class FollowingSpec extends Specification {

  @Subject user = new User("kirk")
  def others = [
    new User("spock"),
    new User("uhura"),
    new User("bones"),
    new User("scotty")
  ]

  def "a new user is not following anyone"() {
    expect:
    user.following.isEmpty()
  }

  def "when a user follows someone they are added to their following list"() {
    when:
    user.follow(others[0])

    then:
    user.following().size() == 1
    user.following().contains(others[0])
  }

  def "a user is only following someone in their following list"() {
    expect:
    !user.follows(others[0])

    when:
    user.follow(others[0])

    then:
    user.follows(others[0])
  }

  def "cannot directly modify the following list"() {
    when:
    user.following() << others[0]

    then:
    thrown UnsupportedOperationException
  }

  def "a user can follow multiple other users"() {
    when:
    others[0..-2].each {
      user.follow(it)
    }

    then:
    user.following.containsAll(others[0..-2])
  }
}
