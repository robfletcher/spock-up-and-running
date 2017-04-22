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
import static java.time.Instant.now

class MessagePostingSpec extends Specification {

  def user = new User("spock")

  def "a user can post a message"() {
    when:
    user.post(messageText, now())

    then:
    user.posts().text == [messageText]

    where:
    messageText = "@kirk that is illogical, Captain!"
  }

  // tag::where-without-iterating-1[]
  def "a user's posts are listed most recent first"() {
    when:
    messages.each {
      user.post(it, now())
    }

    then:
    user.posts().text == messages.reverse()

    where:
    messages = ["It's life, Jim", "but not as we know it"]
  }
  // end::where-without-iterating-1[]

  def "the posting user is recorded in the message"() {
    when:
    user.post("Insults are effective only where emotion is present", now())
    user.post("@bones I was merely stating a fact, Doctor.", now())

    then:
    user.posts().postedBy.every {
      it == user
    }
  }

  def "the list of posts is not modifiable"() {
    when:
    user.posts() << new Message(user, "Fascinating!", now())

    then:
    thrown(UnsupportedOperationException)
  }

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
}
