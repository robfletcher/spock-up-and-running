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

package squawker.notify.trigger

import spock.lang.Subject
import spock.util.concurrent.BlockingVariable
import squawker.User
import squawker.notify.NewFollowerNotifier
import squawker.notify.email.EmailMessage
import squawker.notify.email.EmailSender

class NewFollowerNotifierSpec9 extends BaseTriggerSpec {

  @Subject notifier = new NewFollowerNotifier()

  def "sends email to user when someone follows them"() {
    given:
    def user1 = userStore.insert(username1)
    def user2 = userStore.insert(username2)

    and:
    def toUser = new BlockingVariable<User>()
    def message = new BlockingVariable<EmailMessage>()
    notifier.emailSender = Stub(EmailSender) {
      send(_, _) >> { User to, EmailMessage msg ->
        toUser.set(to)
        message.set(msg)
      }
    }
    eventBus.register(notifier)

    when:
    user2.follow(user1)

    then:
    toUser.get() == user1

    and:
    with(message.get()) {
      from == "admin@squawker.io"
      subject == "You have a new follower!"
      template == "new-follower"
      follower == user2.username
    }

    where:
    username1 = "spock"
    username2 = "kirk"
  }

}
