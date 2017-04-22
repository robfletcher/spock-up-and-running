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

package squawker.test.e2e

import geb.error.PageInstanceNotInitializedException
import spock.lang.FailsWith
import squawker.test.e2e.pages0.ContentMethodsTimelinePage
import squawker.test.e2e.pages0.ContentPropertiesTimelinePage
import squawker.test.e2e.pages1.TimelinePage

class TimelineSpec2 extends BaseWebSpec {
  // tag::intro-page-model[]
  def "a user can see their own messages in their timeline"() {
    given:
    createUser("spock")
    createMessage("spock", "Fascinating!")

    and:
    loginAs("spock")

    when:
    to(TimelinePage)

    then:
    page.messageText == "Fascinating!"
    page.postedBy == "@spock"
  }
  // end::intro-page-model[]

  @FailsWith(PageInstanceNotInitializedException)
  def "demonstrates naive page model that uses properties for content"() {
    given:
    createUser("spock")
    createMessage("spock", "Fascinating!")

    and:
    loginAs("spock")

    when:
    to(ContentPropertiesTimelinePage)

    then:
    page.messageText == "Fascinating!"
    page.postedBy == "@spock"
  }

  def "demonstrates naive page model that uses methods for content"() {
    given:
    createUser("spock")
    createMessage("spock", "Fascinating!")

    and:
    loginAs("spock")

    when:
    to(ContentMethodsTimelinePage)

    then:
    page.messageText == "Fascinating!"
    page.postedBy == "@spock"
  }
}

