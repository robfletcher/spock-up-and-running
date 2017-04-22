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

class MessagePostingSpec extends Specification {

  def user = new User("spock")

  // tag::assert-array-index[]
  def "a user can post a message"() {
    when:
    user.post("@kirk that is illogical, Captain!", now())

    then:
    user.posts.size() == 1
    def message = user.posts[0]
    message.text == "@kirk that is illogical, Captain!"
  }
  // end::assert-array-index[]

  // tag::assert-all-in-array[]
  def "the posting user is recorded in the message"() {
    when:
    user.post("Fascinating!", now())
    user.post("@bones I was merely stating a fact, Doctor.", now())

    then:
    user.posts[0].postedBy == user
    user.posts[1].postedBy == user
  }
  // end::assert-all-in-array[]

  // tag::thrown[]
  def "the list of posts is not modifiable"() {
    when:
    user.posts << new Message(user, "Fascinating!", now())

    then:
    thrown(UnsupportedOperationException)
  }
  // end::thrown[]

  // tag::interrogate-exception[]
  def "a posted message may not be longer than 140 characters"() {
    given:
    def messageText = """Lieutenant, I am half Vulcanian. Vulcanians do not
      speculate. I speak from pure logic. If I let go of a hammer on a planet
      that has a positive gravity, I need not see it fall to know that it has in
      fact fallen."""

    expect:
    messageText.length() > Message.MAX_TEXT_LENGTH

    when:
    user.post(messageText, now())

    then:
    def e = thrown(IllegalArgumentException)
    e.message == "Message text cannot be longer than 140 characters"
  }
  // end::interrogate-exception[]
}
