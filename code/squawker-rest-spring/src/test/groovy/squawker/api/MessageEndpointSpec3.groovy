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

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.TestRestTemplate.HttpClientOption
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.http.converter.HttpMessageConverter
import squawker.Message
import static java.time.Instant.now
import static java.time.format.DateTimeFormatter.ISO_INSTANT
import static org.springframework.http.HttpStatus.*

class MessageEndpointSpec3 extends BaseRestSpecification {

  // tag::sending-data[]
  def "can post a message"() {
    given:
    createUser(username)

    when:
    def request = RequestEntity
      .post(url("/api/$username/messages"))
      .contentType(MediaType.TEXT_PLAIN)
      .body(messageText) // <1>
    def response = client.exchange(request, Message) // <2>

    then:
    response.statusCode == CREATED // <3>

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
  }
  // end::sending-data[]

  // tag::bad-request[]
  def "cannot post a message with no text"() {
    given:
    createUser(username)

    when:
    def request = RequestEntity
      .post(url("/api/$username/messages"))
      .build() // <1>
    def response = client.exchange(request, Map)

    then:
    response.statusCode == BAD_REQUEST // <2>

    where:
    username = "spock"
  }
  // end::bad-request[]

  @Autowired ApplicationContext applicationContext
  @Autowired List<HttpMessageConverter> messageConverters

  // tag::follows-redirect[]
  def "can get a user's latest post"() {
    given:
    // end::follows-redirect[]
    def client = new TestRestTemplate(HttpClientOption.ENABLE_REDIRECTS)
    client.restTemplate.setMessageConverters(messageConverters)
    client.uriTemplateHandler = new LocalHostUriTemplateHandler(applicationContext.environment)
    // tag::follows-redirect[]
    createUser(username)
    def messageId = createMessage(username, messageText) // <1>

    when:
    def response = client.getForEntity("/api/$username/messages/latest", Message)

    then:
    response.statusCode == OK // <2>
    response.body.id == messageId // <3>

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
  }
  // end::follows-redirect[]

  // tag::formatted-timestamp[]
  def "message endpoint renders correct timestamp"() {
    given:
    createUser(username)
    def messageId = createMessage(username, messageText, timestamp) // <1>

    when:
    def response = client.getForEntity("/api/messages/$messageId", Map) // <2>

    then:
    response.statusCode == OK
    response.body.postedAt == ISO_INSTANT.format(timestamp) // <3>

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
    timestamp = now()
  }
  // end::formatted-timestamp[]
}
