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

import spock.lang.Unroll
import squawker.Message
import static org.springframework.http.HttpMethod.*
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.TEXT_PLAIN
import static org.springframework.http.RequestEntity.*

class TimelineEndpointSpec extends BaseRestSpecification {

  def "can read a user's timeline"() {
    given:
    createUser(username)
    def messageId = createMessage(username, messageText)

    when:
    def request = get(url("/api/$username/timeline")).build()
    def response = client.exchange(request, LIST_OF_MESSAGES)

    then:
    response.statusCode == OK
    with(response.body.first()) {
      id == messageId
      text == messageText
      postedBy.username == username
    }

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
  }

  // tag::multi-request-feature[]
  def "a message appears in a user's timeline after they post it"() {
    given:
    createUser(username)

    and:
    def request = post(url("/api/$username/messages"))
      .contentType(TEXT_PLAIN)
      .body(messageText)
    def response = client.exchange(request, Message) // <1>

    expect:
    response.statusCode == CREATED // <2>

    when:
    def request2 = get(url("/api/$username/timeline")).build()
    def response2 = client.exchange(request2, LIST_OF_MESSAGES) // <3>

    then:
    with(response2.body.first()) { // <4>
      text == messageText
      postedBy.username == username
    }

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
  }
  // end::multi-request-feature[]

  @Unroll
  def "cannot use HTTP #method method on timeline endpoint"() {
    given:
    createUser(username)

    when:
    def request = method(requestMethod, url("/api/$username/timeline")).build()
    def response = client.exchange(request, Map)

    then:
    response.statusCode == METHOD_NOT_ALLOWED

    where:
    username = "spock"
    requestMethod << [POST, PUT, DELETE]
  }
}
