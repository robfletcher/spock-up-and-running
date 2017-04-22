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

package squawker.test.e2e.pages1

// tag::first-page-object[]
import geb.Page

class TimelinePage extends Page {

  static url = "#/timeline" // <1>

  static atCheckWaiting = true
  static at = { // <2>
    $(".page-header").text() == "Timeline"
  }

  static content = { // <3>
    messageText {
      $(".sq-message-text").text()
    }
    postedBy {
      $(".sq-posted-by").text()
    }
  }
}
// end::first-page-object[]
