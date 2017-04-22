/*
 * Copyright 2013 the original author or authors.
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
package squawker.jdbi

import spock.lang.Shared
import spock.lang.Subject
import squawker.User

class FollowingStoreSpec extends BasePersistenceSpec {

  @Subject @Shared FollowingStore followingStore

  def setupSpec() {
    followingStore = handle.attach(FollowingStore)
    followingStore.createFollowingTable()
  }

  def cleanupSpec() {
    handle.execute("drop table following if exists")
  }

  def cleanup() {
    handle.execute("delete from following")
  }

  def "can fetch a list of users a user is following"() {
    given:
    def user = new User("spock")
    def followingUser1 = new User("kirk")
    def followingUser2 = new User("bones")
    def nonFollowingUser = new User("khan")
    [user, followingUser1, followingUser2, nonFollowingUser].each {
      userStore.insert(it.username)
    }

    and:
    followingStore.follow(user, followingUser1)
    followingStore.follow(user, followingUser2)

    expect:
    followingStore.findFollowing(user) == [followingUser1, followingUser2]
  }

  def "can fetch a list of users who are following a user"() {
    given:
    def user = new User("spock")
    def follower1 = new User("kirk")
    def follower2 = new User("bones")
    def nonFollower = new User("khan")
    [user, follower1, follower2, nonFollower].each {
      userStore.insert(it.username)
    }

    and:
    followingStore.follow(follower1, user)
    followingStore.follow(follower2, user)

    expect:
    followingStore.findFollowers(user) == [follower1, follower2]
  }
}
