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

package squawker.notify

import spock.lang.Specification
import spock.lang.Subject
import squawker.User
import squawker.notify.email.EmailSender
import static org.hamcrest.Matchers.*

class NewFollowerNotifierSpec8 extends Specification {

  @Subject notifier = new NewFollowerNotifier()

  def "sends email to user when someone follows them"() {
    given:
    def emailSender = Mock(EmailSender)
    notifier.emailSender = emailSender

    when:
    notifier.onNewFollower(event)

    // tag::hamcrest[]
    then:
    1 * emailSender.send(
      user1, // <1>
      allOf( // <2>
        hasProperty("from", equalTo("admin@squawker.io")), // <3>
        hasProperty("subject"), // <4>
        hasProperty("template", equalTo("new-follower")),
        hasProperty("follower", equalTo(user2.username))
      ))
    // end::hamcrest[]

    where:
    user1 = new User("spock")
    user2 = new User("kirk")
    event = new NewFollowerEvent(user1, user2)
  }

}
