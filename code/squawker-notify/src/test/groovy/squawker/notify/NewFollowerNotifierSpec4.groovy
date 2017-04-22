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

package squawker.notify

import spock.lang.Specification
import spock.lang.Subject
import squawker.User
import squawker.notify.email.EmailSender

class NewFollowerNotifierSpec4 extends Specification {

  @Subject notifier = new NewFollowerNotifier()

  def "sends email to user when someone follows them"() {
    // tag::mock-in-given[]
    given:
    def message
    notifier.emailSender = Mock(EmailSender) { // <1>
      1 * send(user1, _) >> { message = it[1] } // <2>
    }

    when:
    notifier.onNewFollower(event)

    then:
    message.from == "admin@squawker.io"
    message.subject == "You have a new follower!"
    message.template == "new-follower"
    message.follower == user2.username
    // end::mock-in-given[]

    where:
    user1 = new User("spock")
    user2 = new User("kirk")
    event = new NewFollowerEvent(user1, user2)
  }

}
