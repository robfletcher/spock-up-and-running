/*
 * Copyright 2014 the original author or authors.
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

package squawker.api

import squawker.Message
import static java.time.Instant.now
import static org.springframework.http.HttpHeaders.LOCATION
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.TEXT_PLAIN
import static org.springframework.http.RequestEntity.post

class MessageEndpointSpec4 extends BaseRestSpecification {

  // tag::test-response-data[]
  def "a user can post a message"() {
    given:
    createUser(username)

    when:
    def request = post(url("/api/$username/messages"))
      .contentType(TEXT_PLAIN)
      .body(messageText)
    def response = client.exchange(request, Message) // <1>

    then:
    response.statusCode == CREATED

    and:
    with(response.body) { // <2>
      postedBy.username == username // <3>
      text == messageText
    }

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
  }
  // end::test-response-data[]

  def "can find an individual message"() {
    given:
    createUser(username)
    def messageId = createMessage(username, messageText)

    when:
    def response = client.getForEntity("/api/messages/$messageId", Message)

    then:
    response.statusCode == OK
    with(response.body) {
      postedBy.username == username
      text == messageText
    }

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
  }

  def "message endpoint renders correct timestamp"() {
    given:
    createUser(username)
    def messageId = createMessage(username, messageText, timestamp)

    // tag::parsed-timestamp[]
    when:
    def response = client.getForEntity("/api/messages/$messageId", Message)

    then:
    response.statusCode == OK
    response.body.postedAt == timestamp
    // end::parsed-timestamp[]

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
    timestamp = now()
  }

  // tag::asserts-redirect[]
  def "can get a user's latest post"() {
    given:
    createUser(username)
    def messageId = createMessage(username, messageText)

    when:
    def response = client.getForEntity("/api/$username/messages/latest", String)

    then:
    with(response) {
      statusCode == FOUND // <1>
      headers.getFirst(LOCATION).toURI() == url("/api/messages/$messageId")
      // <2>
    }

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
  }
  // end::asserts-redirect[]
}
