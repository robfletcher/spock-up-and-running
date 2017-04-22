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

class FollowingSpec3 extends Specification {

  // tag::spec-fields[]
  def user = new User("kirk") // <1>
  def other = new User("spock")

  def "a user can follow another user"() {
    when:
    user.follow(other)

    then:
    user.following.size() == 1
    user.following.contains(other)
  }

  def "a user reports if they are following someone"() {
    expect:
    !user.follows(other)

    when:
    user.follow(other)

    then:
    user.follows(other)
  }
  // end::spec-fields[]
}
