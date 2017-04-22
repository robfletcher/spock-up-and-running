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

import com.oreilly.spock.FailsWithCondition
import spock.lang.Specification

class FollowingSpec4 extends Specification {

  def user = new User("kirk")
  def other = new User("spock")

  @FailsWithCondition("""\
user.following[0] == other.toString()
|    |        |   |  |     |
|    [@spock] |   |  |     @spock (java.lang.String)
@kirk         |   |  @spock
              |   false
              @spock (squawker.User)
""")
  def "a user can follow another user"() {
    // tag::power-assert-types[]
    when:
    user.follow(other)

    then:
    user.following[0] == other.toString()
    // end::power-assert-types[]
  }
}
