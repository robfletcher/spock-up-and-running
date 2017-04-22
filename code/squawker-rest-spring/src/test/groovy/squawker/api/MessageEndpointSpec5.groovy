/*
 * Copyright 2016 the original author or authors.
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

import org.skife.jdbi.v2.Handle
import org.springframework.beans.factory.annotation.Autowired
import squawker.Message
import squawker.jdbi.ApiTokenStore
import static org.springframework.http.HttpHeaders.AUTHORIZATION
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.TEXT_PLAIN
import static org.springframework.http.RequestEntity.delete
import static org.springframework.http.RequestEntity.post

class MessageEndpointSpec5 extends BaseRestSpecification {

  // tag::gen-token[]
  @Autowired ApiTokenStore apiTokenStore // <1>

  protected String generateToken(String username) {
    def user = userStore.find(username)
    def token = apiTokenStore.generateTokenFor(user) // <2>
    "Token $token" // <3>
  }

  def cleanup() {
    dbi.open().withCloseable { Handle handle ->
      // ... existing code
      handle.execute("delete from api_token") // <4>
    }
  }
  // end::gen-token[]

  // tag::reject-unauthenticated[]
  def "an anonymous user cannot post a message"() {
    given:
    createUser(username)

    when:
    def request = post(url("/api/messages"))
      .contentType(TEXT_PLAIN)
      .body(messageText) // <1>
    def response = client.exchange(request, Map)

    then:
    response.statusCode == UNAUTHORIZED // <2>

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
  }
  // end::reject-unauthenticated[]

  // tag::authenticated[]
  def "a user can post a message"() {
    given:
    createUser(username)
    def authToken = generateToken(username) // <1>

    when:
    def request = post(url("/api/messages")) // <2>
      .header(AUTHORIZATION, authToken) // <3>
      .contentType(TEXT_PLAIN)
      .body(messageText)
    def response = client.exchange(request, Message)

    then:
    response.statusCode == CREATED

    and:
    with(response.body) {
      postedBy.username == username // <4>
      text == messageText
    }

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
  }
  // end::authenticated[]

  def "a user cannot delete a non-existent message"() {
    given:
    createUser(username)
    def authToken = generateToken(username)

    when:
    def request = delete(url("/api/messages/1234"))
      .header(AUTHORIZATION, authToken)
      .build()
    def response = client.exchange(request, Map)

    then:
    response.statusCode == NOT_FOUND

    where:
    username = "spock"
  }

  def "an anonymous user cannot delete a message"() {
    given:
    createUser(username)
    def messageId = createMessage(username, messageText)
    def messageUrl = url("/api/messages/$messageId")

    when:
    def request = delete(messageUrl).build()
    def response = client.exchange(request, Map)

    then:
    response.statusCode == UNAUTHORIZED

    and:
    client
      .getForEntity(messageUrl, Map)
      .statusCode == OK

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
  }

  // tag::delete-message[]
  def "a user can delete their own message"() {
    given:
    createUser(username)
    def authToken = generateToken(username)
    def messageId = createMessage(username, messageText)
    def messageUrl = url("/api/messages/$messageId")

    when:
    def request = delete(messageUrl) // <1>
      .header(AUTHORIZATION, authToken)
      .build()
    def response = client.exchange(request, Map)

    then:
    response.statusCode == OK // <2>

    and:
    client.getForEntity(messageUrl, Map).statusCode == NOT_FOUND // <3>

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
  }
  // end::delete-message[]

  // tag::authorization-fail[]
  def "a user cannot delete another user's message"() {
    given:
    createUser(postingUser)
    def messageId = createMessage(postingUser, messageText) // <1>
    def messageUrl = url("/api/messages/$messageId")

    and:
    createUser(requestingUser)
    def authToken = generateToken(requestingUser)

    when:
    def request = delete(messageUrl)
      .header(AUTHORIZATION, authToken) // <2>
      .build()
    def response = client.exchange(request, Map)

    then:
    response.statusCode == FORBIDDEN // <3>

    and:
    client.getForEntity(messageUrl, Map).statusCode == OK // <4>

    where:
    postingUser = "spock"
    requestingUser = "kirk"
    messageText = "@kirk That is illogical, Captain!"
  }
  // end::authorization-fail[]
}
