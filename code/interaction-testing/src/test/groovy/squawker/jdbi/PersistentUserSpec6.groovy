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

import spock.lang.*
import squawker.*
import static java.time.Instant.*

class PersistentUserSpec6 extends Specification {

  def dataStore = Mock(DataStore)
  @Subject user = new PersistentUser(dataStore, "spock", now())

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
    // tag::wildcard-all[]
    0 * _
    // end::wildcard-all[]
  }
}
