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

import java.time.*
import spock.lang.*

class UserSpec extends Specification {

  // tag::assert-with-block[]
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
  // end::assert-with-block[]
}
