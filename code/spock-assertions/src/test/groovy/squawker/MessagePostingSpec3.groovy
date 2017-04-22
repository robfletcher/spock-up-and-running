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
import static java.time.Instant.*

class MessagePostingSpec3 extends Specification {

  def user = new User("spock")

  // tag::every-with-implicit-collect[]
  def "the posting user is recorded in the message"() {
    when:
    user.post("Fascinating!", now())
    user.post("@bones I was merely stating a fact, Doctor.", now())

    then:
    user.posts.postedBy.every {
      it == user
    }
  }
  // end::every-with-implicit-collect[]
}
