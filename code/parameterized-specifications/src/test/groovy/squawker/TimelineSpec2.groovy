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
package squawker

import spock.lang.*
import squawker.jdbi.*

class TimelineSpec2 extends BasePersistenceSpec {

  @Shared @Subject User user
  @Shared User followedUser
  @Shared User notFollowedUser

  def setupSpec() {
    user = dataStore.newUser("spock")
    followedUser = dataStore.newUser("kirk")
    notFollowedUser = dataStore.newUser("khan")

    user.follow(followedUser)
  }

  def cleanup() {
    handle.execute("delete from message")
  }

  // tag::where-assignment[]
  @Unroll
  def "a user only sees messages from users they follow in their timeline"() {
    given:
    dataStore.insert(message) // <2>

    expect:
    user.timeline().contains(message) == shouldAppearInTimeline

    where:
    postedBy        | shouldAppearInTimeline
    user            | true
    followedUser    | true
    notFollowedUser | false

    message = new Message(postedBy, "Lorem ipsum dolor sit amet") // <1>
  }
  // end::where-assignment[]
}
