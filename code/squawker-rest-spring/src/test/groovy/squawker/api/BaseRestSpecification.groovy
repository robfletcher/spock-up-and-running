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

import java.time.Clock
import java.time.Instant
import org.skife.jdbi.v2.DBI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import spock.lang.Specification
import squawker.Message
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore
import static java.time.Instant.now
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [Main])
abstract class BaseRestSpecification extends Specification {

  // tag::param-type-ref[]
  protected final ParameterizedTypeReference<List<Message>> LIST_OF_MESSAGES =
    new ParameterizedTypeReference<List<Message>>() {}
  // end::param-type-ref[]

  // tag::port-and-url[]
  @LocalServerPort int port

  protected URI url(String relativeUrl) {
    "http://localhost:$port$relativeUrl".toURI()
  }
  // end::port-and-url[]

  @Autowired TestRestTemplate client

  @Autowired DBI dbi
  @Autowired UserStore userStore
  @Autowired MessageStore messageStore
  @Autowired Clock clock

  def cleanup() {
    dbi.open().withCloseable { handle ->
      handle.execute("delete from message")
      handle.execute("delete from following")
      handle.execute("delete from user")
    }
  }

  // tag::remote-helper-method[]
  protected void createUser(String username) {
    userStore.insert(username)
  }
  // end::remote-helper-method[]

  // tag::return-from-remote[]
  protected Serializable createMessage(String username,
                                       String text,
                                       Instant postedAt = now()) {
    def user = userStore.find(username)
    messageStore.insert(user, text, postedAt).id
  }
  // end::return-from-remote[]
}
