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

import org.skife.jdbi.v2.Handle
import org.skife.jdbi.v2.exceptions.UnableToCreateStatementException
import spock.lang.Specification
import spock.lang.Subject
import squawker.Message
import squawker.User
import static java.time.Instant.now
import static org.hamcrest.Matchers.equalTo

class PersistentUserSpec extends Specification {

  def userStore = Mock(UserStore)
  def messageStore = Mock(MessageStore)
  def followingStore = Mock(FollowingStore)

  def handle = Stub(Handle) {
    attach(UserStore) >> userStore
    attach(MessageStore) >> messageStore
    attach(FollowingStore) >> followingStore
  }

  @Subject user = new PersistentUser(1L, handle, "spock", now())

  def "following another user is persisted"() {
    given:
    def other = new User("kirk")

    when:
    user.follow(other)

    then:
    1 * followingStore.follow(user, other)
  }

  def "users cannot follow themselves"() {
    when:
    user.follow(user)

    then:
    thrown(IllegalArgumentException)

    and:
    0 * followingStore.follow(*_)
  }

  def "timeline is fetched from database"() {
    given:
    def poster = new User("kirk")
    def message = new Message(poster, "@spock damage report, Mr Spock", now())

    when:
    def timeline = user.timeline()

    then:
    1 * messageStore.timeline(user) >> [message]

    and:
    timeline == [message]
  }

  def "following list is read from database and cached"() {
    given:
    def otherUsers = ["kirk", "bones", "scotty"].collect {
      new User(it)
    }

    when:
    def result1 = user.following()
    def result2 = user.following()

    then:
    1 * followingStore.findFollowing(user) >> otherUsers

    and:
    result1 == otherUsers as Set
    result2 == result1
  }

  def "posting a message inserts it to the database"() {
    given:
    def messageText = "Fascinating!"

    when:
    user.post(messageText, now())

    then:
    1 * messageStore.insert(equalTo(user), equalTo(messageText), _)
  }

  def "a message that is too long is not written to the database"() {
    given: "some message text that exceeds the maximum allowed length"
    def messageText = """On my planet, 'to rest' is to rest, to cease using
                         energy. To me it is quite illogical to run up and down
                         on green grass using energy instead of saving it."""

    expect:
    messageText.length() > Message.MAX_TEXT_LENGTH

    when: "a user attempts to post the message"
    user.post(messageText, now())

    then: "an exception is thrown"
    thrown(IllegalArgumentException)

    and: "no attempt is made to write the message to the database"
    0 * _
  }

  def "an exception is thrown if the database connection is stale"() {
    given:
    messageStore._ >> {
      throw new UnableToCreateStatementException(null)
    }

    when:
    user.posts()

    then:
    def e = thrown(IllegalStateException)
    e.cause instanceof UnableToCreateStatementException
  }
}
