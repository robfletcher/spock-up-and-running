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

import spock.lang.*

class FollowingSpec extends Specification {

  // tag::first-spec[]
  def "a user can follow another user"() {
    given:
    def user = new User("kirk")
    def other = new User("spock")

    when:
    user.follow(other)

    then:
    user.following.size() == 1
    user.following.contains(other)
  }
  // end::first-spec[]

  // tag::simple-spec[]
  def "a new user is not following anyone"() {
    expect:
    new User("kirk").following.isEmpty()
  }
  // end::simple-spec[]
}
