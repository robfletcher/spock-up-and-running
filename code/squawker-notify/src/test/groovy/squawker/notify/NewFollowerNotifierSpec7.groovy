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

import java.util.concurrent.atomic.AtomicReference
import spock.lang.Specification
import spock.lang.Subject
import squawker.User
import squawker.notify.email.EmailMessage
import squawker.notify.email.EmailSender

class NewFollowerNotifierSpec7 extends Specification {

  @Subject notifier = new NewFollowerNotifier()

  def "sends email to user when someone follows them"() {
    // tag::atomic-ref-capture[]
    given:
    def message = new AtomicReference<EmailMessage>()
    notifier.emailSender = Mock(EmailSender) {
      1 * send(user1, _) >> { _, EmailMessage msg -> message.set(msg) }
    }
    // end::atomic-ref-capture[]

    when:
    notifier.onNewFollower(event)

    then:
    with(message.get()) {
      from == "admin@squawker.io"
      subject == "You have a new follower!"
      template == "new-follower"
      follower == user2.username
    }

    where:
    user1 = new User("spock")
    user2 = new User("kirk")
    event = new NewFollowerEvent(user1, user2)
  }

}
