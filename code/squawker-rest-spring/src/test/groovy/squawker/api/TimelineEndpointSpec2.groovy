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

import org.skife.jdbi.v2.Handle
import squawker.Message
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.MediaType.TEXT_PLAIN
import static org.springframework.http.RequestEntity.post

class TimelineEndpointSpec2 extends BaseRestSpecification {

  // tag::verify-with-remote[]
  def "a message appears in a user's timeline after they post it"() {
    given:
    createUser(username)

    when:
    def request = post(url("/api/$username/messages"))
      .contentType(TEXT_PLAIN)
      .body(messageText)
    def response = client.exchange(request, Message)

    then:
    response.statusCode == CREATED

    and:
    def message = latestMessage() // <1>
    message.text == messageText
    message.username == username

    where:
    username = "spock"
    messageText = "@kirk That is illogical, Captain!"
  }

  private Map<String, Object> latestMessage() { // <2>
    dbi.open().withCloseable { Handle handle ->
      handle.createQuery("""select m.text, u.username
                             from message m, user u
                            where u.id = m.posted_by_id
                            order by m.posted_at desc""")
            .first()
    }
  }
  // end::verify-with-remote[]
}
